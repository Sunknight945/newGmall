package com.atguigu.gmall.index.aop.annotation;

import java.lang.annotation.*;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GmallCache {

	/**
	 * 缓存key的前缀
	 *
	 * @return
	 */
	String prefix() default "";

	/**
	 * 缓存的过期时间  以分钟为单位
	 *
	 * @return
	 */
	int timeout() default 5;

	/**
	 * 为了防止缓存雪崩 设置存缓存时加上的时间的随机值的范围
	 *Gallery 图库
	 * @return
	 */
	int random() default 5;

}


