item 模块的作用是从其他的微服务里面获取组成item商品项的元素
然后拼接返回 , 所以它并不需要 自己的数据做为数据来源的支撑.

这里会涉及到 异步编排 以提高 数据库 层次的链路操作 的时间开销.