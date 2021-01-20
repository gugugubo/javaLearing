

# 一些重要的概念



### [索引](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=索引)

什么是数据库索引？https://www.cnblogs.com/wwxzdl/p/11116446.html



### [查询缓存的使用](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=查询缓存的使用)





# 优化处理



## 并发事务

### [什么是事务?](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=什么是事务)

### [事物的四大特性(ACID)](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=事物的四大特性acid)

### [并发事务带来哪些问题?](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=并发事务带来哪些问题)



## 并发事务的解决方案

### [事务隔离级别有哪些?MySQL的默认隔离级别是?](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=事务隔离级别有哪些mysql的默认隔离级别是)

### [锁机制与InnoDB锁算法](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=锁机制与innodb锁算法)

交接更多锁的分类？：https://blog.csdn.net/qq_34337272/article/details/80611486



## 性能？

### [大表优化](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=大表优化)

#### [1. 限定数据的范围](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=1-限定数据的范围)

#### [2. 读/写分离](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=2-读写分离)

#### [3. 垂直分区](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=3-垂直分区)

#### [4. 水平分区](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=4-水平分区)

垂直分区和水平分区（即数据分片）之后可以进行分库或者分表，垂直分区和水平分区之后的事务问题都难以解决

- 更多优化相关知识： https://segmentfault.com/a/1190000006158186#item-8



#### 5. 分库分表带来的问题

- ##### [分库分表之后,id 主键如何处理？](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=分库分表之后id-主键如何处理？)

- 更多相关知识：https://www.cnblogs.com/shizhuoping/p/11563948.html



### [解释一下什么是池化设计思想。什么是数据库连接池?为什么需要数据库连接池?](https://snailclimb.gitee.io/javaguide/#/docs/database/MySQL?id=解释一下什么是池化设计思想。什么是数据库连接池为什么需要数据库连接池)

- 更多相关知识：https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485679&idx=1&sn=57dbca8c9ad49e1f3968ecff04a4f735&chksm=cea24724f9d5ce3212292fac291234a760c99c0960b5430d714269efe33554730b5f71208582&token=1141994790&lang=zh_CN#rd