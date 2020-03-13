# 使用redis的原因

### [为什么要用 redis/为什么要用缓存](https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/Redis?id=为什么要用-redis为什么要用缓存)

### [为什么要用 redis 而不用 map/guava 做缓存?](https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/Redis?id=为什么要用-redis-而不用-mapguava-做缓存)

### [redis 和 memcached 的区别](https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/Redis?id=redis-和-memcached-的区别)





# redis使用

## 基本使用

### [redis 常见数据结构以及使用场景分析](https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/Redis?id=redis-常见数据结构以及使用场景分析)

### [redis 设置过期时间](https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/Redis?id=redis-设置过期时间)

### [redis 内存淘汰机制(MySQL里有2000w数据，Redis中只存20w的数据，如何保证Redis中的数据都是热点数据?)](https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/Redis?id=redis-内存淘汰机制mysql里有2000w数据，redis中只存20w的数据，如何保证redis中的数据都是热点数据)



## 数据安全

### [redis 持久化机制(怎么保证 redis 挂掉之后再重启数据可以进行恢复)](https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/Redis?id=redis-持久化机制怎么保证-redis-挂掉之后再重启数据可以进行恢复)







### [redis 事务](https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/Redis?id=redis-事务)







### [缓存雪崩和缓存穿透问题解决方案](https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/Redis?id=缓存雪崩和缓存穿透问题解决方案)





### [如何解决 Redis 的并发竞争 Key 问题](https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/Redis?id=如何解决-redis-的并发竞争-key-问题)

分布式锁：

-  使用redis实现分布式锁的原理：https://snhttps://www.cnblogs.com/0201zcr/p/5942748.htmlailclimb.gitee.io/javaguide/#/docs/database/Redis/Redlock%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81
- redlock（即使用redis实现的分布式锁） 的思考：https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/%E5%A6%82%E4%BD%95%E5%81%9A%E5%8F%AF%E9%9D%A0%E7%9A%84%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%EF%BC%8CRedlock%E7%9C%9F%E7%9A%84%E5%8F%AF%E8%A1%8C%E4%B9%88
- https://www.cnblogs.com/0201zcr/p/5942748.html
- 使用redis实现分布式锁的原理：https://www.jianshu.com/p/a1ebab8ce78a



### [如何保证缓存与数据库双写时的数据一致性?](https://snailclimb.gitee.io/javaguide/#/docs/database/Redis/Redis?id=如何保证缓存与数据库双写时的数据一致性)

https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/redis-consistence.md