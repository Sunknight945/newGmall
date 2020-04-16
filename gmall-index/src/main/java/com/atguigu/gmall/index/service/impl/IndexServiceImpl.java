package com.atguigu.gmall.index.service.impl;


import cn.hutool.core.util.StrUtil;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.index.aop.annotation.GmallCache;
import com.atguigu.gmall.index.feign.GmallPmsClient;
import com.atguigu.gmall.index.service.IndexService;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.vo.CategoryEntityVo;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Service
public class IndexServiceImpl implements IndexService {

	@Autowired
	private GmallPmsClient pmsClient;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private RedissonClient redissonClient;

	public static final String key_prefix = "index:cates:";

	/**
	 * 查询一级分类
	 *
	 * @return
	 */
	@Override
	public List<CategoryEntity> queryLv1Categories() {
		Resp<List<CategoryEntity>> categoryListByParenCidAndLevel = this.pmsClient.queryCategoryListByParenCidAndLevel(1, null);
		return categoryListByParenCidAndLevel.getData();
	}

	/**
	 * 查询一级分类下的子分类
	 * 把缓存和检验是否缓存与分布式锁(这样的脏活)交给aop来处理 哈哈哈 ...
	 * 这边只要配上一个自定义的注解@annotation 配合自定义的切面Aspect 来进行传参 就可以了.
	 *
	 * @param pid 父级分类的id
	 */
	@Override
	@GmallCache(prefix = "index:cates:", timeout = 5 * 24 * 60, random = 100)
	public List<CategoryEntityVo> querySubCategories(Long pid) {
		Resp<List<CategoryEntityVo>> subCategoriesByPid = this.pmsClient.querySubCategoriesByPid(pid);
		return subCategoriesByPid.getData();
	}


	/**
	 * 测试锁
	 */
	@Override
	public void testLock() {
		RLock lock = this.redissonClient.getLock("lock");
		lock.lock();
		String str = this.redisTemplate.opsForValue().get(key_prefix + "1");
		String num = this.redisTemplate.opsForValue().get("num");
		if (StrUtil.isBlank(str)) {
			return;
		}
		assert num != null;
		Integer numN = Integer.valueOf(num);
		numN++;
		this.redisTemplate.opsForValue().set("num", String.valueOf(numN));
		lock.unlock();
	}

	@Override
	public String testRead() {
		RReadWriteLock rwLock = this.redissonClient.getReadWriteLock("rwLock");
		rwLock.readLock().lock(10L, TimeUnit.SECONDS);
		String test = this.redisTemplate.opsForValue().get("test");
		// rwLock.readLock().unlock();
		return test;
	}

	@Override
	public String testWrite() {
		RReadWriteLock rwLock = this.redissonClient.getReadWriteLock("rwLock");
		rwLock.writeLock().lock(10L, TimeUnit.SECONDS);
		this.redisTemplate.opsForValue().set("test", UUID.randomUUID().toString());
		// rwLock.writeLock().unlock();
		return "写入了数据 ok";
	}
}


