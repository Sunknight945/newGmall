package com.atguigu.gmall.index.aop.aspect;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.index.aop.annotation.GmallCache;
import com.atguigu.gmall.pms.vo.CategoryEntityVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Component
@Aspect
public class GmallCacheAspect {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private RedissonClient redissonClient;

	public static final String key_prefix = "index:cates:";


	@Around("@annotation(com.atguigu.gmall.index.aop.annotation.GmallCache)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		// Object[] proxyMethod_pid = joinPoint.getArgs();
		Object[] args = joinPoint.getArgs();
		List<Object> asList = Arrays.asList(args);
		Object o = asList.get(0);
		String proxyMethod_pid = String.valueOf(o);
		// String pid = String.valueOf(proxyMethod_pid);
		//1 先校验没有有需要的pid 的缓存 有的话直接放回
		boolean flag = checkCache(proxyMethod_pid);
		if (flag) {
			return getCategoryEntityVoList(proxyMethod_pid);
		}
		//2 如果没有的话则加上一把redisson 的锁来确保 mysql 不会在这一瞬间被击穿
		RLock lock = this.redissonClient.getLock("lock" + proxyMethod_pid);
		lock.lock();
		//3 若果在上面的那一瞬间 没有别的机器或者集群进行了进行mysql 查询的话 就 在自己这个线程来查询 确保 mysql 与redis 的对向 安全
		flag = checkCache(proxyMethod_pid);
		if (flag) {
			lock.unlock();
			return getCategoryEntityVoList(proxyMethod_pid);
		} else {
			//4 以上都没有结果就从mysql查了
			Object categoryVoList = packCategoryCoListFromMysql(joinPoint, methodSignature, proxyMethod_pid);
			lock.unlock();
			return categoryVoList;
		}
	}

	private Object packCategoryCoListFromMysql(ProceedingJoinPoint joinPoint, MethodSignature methodSignature, String proxyMethod_pid) throws Throwable {
		Method method = methodSignature.getMethod();
		GmallCache gmallCache = method.getAnnotation(GmallCache.class);
		String prefix = gmallCache.prefix();
		int random = gmallCache.random();
		int timeout = gmallCache.timeout();
		//如果把这个切入点的 执行方法屏蔽之后 那么 被代理的切入点的方法也不会被执行 ... 这就很神奇了. made
		Object categoryVoList = joinPoint.proceed(joinPoint.getArgs());
		String categoryVoListStr = JSON.toJSONString(categoryVoList);
		this.stringRedisTemplate.opsForValue().set(key_prefix + proxyMethod_pid, categoryVoListStr, timeout + new Random().nextInt(random), TimeUnit.HOURS);
		return categoryVoList;
	}

	private List<CategoryEntityVo> getCategoryEntityVoList(String proxyMethod_pid) {
		return JSON.parseArray(this.stringRedisTemplate.opsForValue().get(key_prefix + proxyMethod_pid), CategoryEntityVo.class);
	}

	private boolean checkCache(String proxyMethod_pid) {
		return this.stringRedisTemplate.hasKey(key_prefix + proxyMethod_pid);
	}
}


