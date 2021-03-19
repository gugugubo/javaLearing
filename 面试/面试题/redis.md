



1. redis的数据结构：

   1. 什么是跳表？https://mp.weixin.qq.com/s/Ok0laJMn4_OzL-LxPTHawQ

      1. 如果链表的结点数量非常多，我们就可以抽出更多的索引层级，每一层索引的结点数量都是低层索引的一半，删除和插入需要维护索引
      2. 程序中跳表采用的是双向链表，无论前后结点还是上下结点，都各有两个指针相互指向彼此。
      3. 程序中跳表的每一层首位各有一个空结点，左侧的空节点是负无穷大，右侧的空节点是正无穷大。

   2. 五种数据结 

   3. redisObject  

      ```
      typedef struct redisObject{
           //类型
           unsigned type:4;
           //编码
           unsigned encoding:4;
           //指向底层数据结构的指针
           void *ptr;
           //引用计数
           int refcount;
           //记录最后一次被程序访问的时间
           unsigned lru:22;
      }robj
      ```

      

2. redis的线程模型？文件事件处理器的结构包含 4 个部分：1.多个 socket（客户端连接），2.IO 多路复用程序（支持多个客户端连接的关键），3.文件事件分派器（将 socket 关联到相应的事件处理器）4.事件处理器（连接应答处理器、命令请求处理器、命令回复处理器）---处理流程为：当被监听的套接字准备好执行连接应答（accept）、读取（read）、写入（write）、关 闭（close）等操作时，与操作相对应的文件事件就会产生，这时文件事件处理器就会调用套接字之前关联好的事件处理器来处理这些事件 

3.  为啥 redis 单线程模型也能效率这么高？

   1. 第一，纯内存访问，`Redis`将所有数据放在内存中，内存的响应时长大约为100纳秒，这是Redis达到每秒万级别访问的重要基础。
   2. 第二，非阻塞`I/O`，`Redis`使用`epoll`作为`I/O`多路复用技术的实现，再加上`Redis`自身的事件处理模型将`epoll`中的连接、读写、关闭都转换为事件。
   3. 第三，单线程避免了线程切换和竞态产生的消耗。既然采用单线程就能达到如此高的性能，那么也不失为一种不错的选择，因为单线程能带来几个好处：第一，单线程可以简化数据结构和算法的实现。如果对高级编程语言熟悉的读者应该了解并发数据结构实现不但困难而且开发测试比较麻烦。第二，单线程避免了线程切换和竞态产生的消耗，对于服务端开发来说，锁和线程切换通常是性能杀手。
   
4. Redis是如何判断数据是否过期的呢？

   Redis  通过一个叫做过期字典（可以看作是hash表）来保存数据过期的时间。过期字典的键指向Redis数据库中的某个key(键)，过期字典的值是一个long long类型的整数，这个整数保存了key所指向的数据库键的过期时间（毫秒精度的UNIX时间戳）。 

5. 过期的数据的删除策略了解么？

   1. 惰性删除 ：只会在取出key的时候才对数据进行过期检查。这样对CPU最友好，但是可能会造成太多过期 key 没有被删除。
   2. 定期删除 ： 每隔一段时间抽取一批 key 执行删除过期key操作。并且，Redis 底层会通过限制删除操作执行的时长和频率来减少删除操作对CPU时间的影响。

   但是，仅仅通过给 key 设置过期时间还是有问题的。因为还是可能存在定期删除和惰性删除漏掉了很多过期  key 的情况。这样就导致大量过期 key 堆积在内存里，然后就Out of memory了。

   怎么解决这个问题呢？答案就是： Redis 内存淘汰机制。（不是很懂）

6. Redis 内存淘汰机制了解么？

   Redis 提供 6 种数据淘汰策略：

   1. volatile-lru（least recently used）：从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰
   2. volatile-ttl：从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
   3. volatile-random：从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰
   4. allkeys-lru（least recently used）：当内存不足以容纳新写入数据时，在键空间中，移除最近最少使用的 key（这个是最常用的）
   5. allkeys-random：从数据集（server.db[i].dict）中任意选择数据淘汰
   6. **no-**eviction****：禁止驱逐数据，也就是说当内存不足以容纳新写入数据时，新写入操作会报错。这个应该没人使用吧！

   4.0 版本后增加以下两种：

   1. volatile-lfu（least frequently used）：从已设置过期时间的数据集(server.db[i].expires)中挑选最不经常使用的数据淘汰
   2. allkeys-lfu（least frequently used）：当内存不足以容纳新写入数据时，在键空间中，移除最不经常使用的 key

7. Redis 持久化机制(怎么保证 Redis 挂掉之后再重启数据可以进行恢复)

   Redis 的一种持久化方式叫快照（snapshotting，RDB），另一种方式是只追加文件（append-only file, AOF）

   1. 快照（snapshotting）持久化（RDB）：快照持久化是 Redis 默认采用的持久化方式，在 Redis.conf 配置文件中默认有此下配置：

      ```conf
      save 900 1           #在900秒(15分钟)之后，如果至少有1个key发生变化，Redis就会自动触发BGSAVE命令创建快照。
      save 300 10          #在300秒(5分钟)之后，如果至少有10个key发生变化，Redis就会自动触发BGSAVE命令创建快照。
      save 60 10000        #在60秒(1分钟)之后，如果至少有10000个key发生变化，Redis就会自动触发BGSAVE命令创建快
      ```

   2. AOF（append-only file）持久化：与快照持久化相比，AOF 持久化 的实时性更好，因此已成为主流的持久化方案。默认情况下 Redis 没有开启 AOF（append only file）方式的持久化，可以通过 appendonly 参数开启：appendonly yes

      在 Redis 的配置文件中存在三种不同的 AOF 持久化方式，它们分别是：

      ```conf
      appendfsync always    #每次有数据修改发生时都会写入AOF文件,这样会严重降低Redis的速度
      appendfsync everysec  #每秒钟同步一次，显示地将多个写命令同步到硬盘
      appendfsync no        #让操作系统决定何时进行同步
      ```

   3. **补充内容：AOF 重写**

      AOF 重写可以产生一个新的 AOF 文件，这个新的 AOF 文件和原有的 AOF 文件所保存的数据库状态一样，但体积更小。

      AOF 重写是一个有歧义的名字，该功能是通过读取数据库中的键值对来实现的，程序无须对现有 AOF 文件进行任何读入、分析或者写入操作。

      在执行 BGREWRITEAOF 命令时，Redis 服务器会维护一个 AOF 重写缓冲区，该缓冲区会在子进程创建新 AOF  文件期间，记录服务器执行的所有写命令。当子进程完成创建新 AOF 文件的工作之后，服务器会将重写缓冲区中的所有内容追加到新 AOF  文件的末尾，使得新旧两个 AOF 文件所保存的数据库状态一致。最后，服务器用新的 AOF 文件替换旧的 AOF 文件，以此来完成 AOF  文件重写操作

8. 缓存穿透

   1. 缓存穿透说简单点就是大量请求的 key 根本不存在于缓存中，导致请求直接到了数据库上，根本没有经过缓存这一层。举个例子：某个黑客故意制造我们缓存中不存在的 key 发起大量请求，导致大量请求落到数据库。
   2. 如何解决缓存穿透
      1. 缓存无效 key（感觉没用）
      2. 布隆过滤器

9.  缓存雪崩

   1. 实际上，缓存雪崩描述的就是这样一个简单的场景：缓存在同一时间大面积的失效，后面的请求都直接落到了数据库上，造成数据库短时间内承受大量请求。
   2. 有哪些解决办法？
      1. 针对 Redis 服务不可用的情况：
         1. 采用 Redis 集群，避免单机出现问题整个缓存服务都没办法使用。
         2. 限流，避免同时处理大量的请求。
      2. 针对热点缓存失效的情况：
         1. 设置不同的失效时间比如随机设置缓存的失效时间。
         2. 缓存永不失效。

10. 如何保证缓存和数据库数据的一致性？

    下面单独对  Cache Aside Pattern（旁路缓存模式） 来聊聊。

    Cache Aside Pattern 中遇到写请求是这样的：更新 DB，然后直接删除 cache 。

    如果更新数据库成功，而删除缓存这一步失败的情况的话，简单说两个解决方案：

    1. 缓存失效时间变短（不推荐，治标不治本） ：我们让缓存数据的过期时间变短，这样的话缓存就会从数据库中加载数据。另外，这种解决办法对于先操作缓存后操作数据库的场景不适用。
    2. 增加cache更新重试机制（常用）： 如果 cache 服务当前不可用导致缓存删除失败的话，我们就隔一段时间进行重试，重试次数可以自己定。如果多次重试还是失败的话，我们可以把当前更新失败的 key 存入队列中，等缓存服务可用之后，再将 缓存中对应的 key 删除即可。
    
11. redis底层数据结构：https://mp.weixin.qq.com/s/FtfAqXXDef6-bhuGyPDK7w  https://mp.weixin.qq.com/s/JvL2IOUu8mGLfeahHaRBFg

    ![https://gitee.com/gu_chun_bo/picture/raw/master/image/20200417125108-879609.png](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200417125108-879609.png)

    1. 简单动态字符串

       ![图片](https://mmbiz.qpic.cn/mmbiz_png/g6hBZ0jzZb0Zb0XiaaR6bGaN80wicXIIP7Diay6tbe99SxEdCbyfMItmJNEDgxQ3iayqmSyEZ8q6IIsibbNQJtP8AcQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

       1. 字符串长度处理，用一个 len 字段记录当前字符串的长度
       2. 内存重新分配，Redis 中会涉及到字符串频繁的修改操作， SDS 实现了两种优化策略1.空间预分配  2.惰性空间释放
       3. 二进制安全，通过len解决

    2. 双端链表

       ![图片](https://mmbiz.qpic.cn/mmbiz_png/g6hBZ0jzZb0Zb0XiaaR6bGaN80wicXIIP70cib2FMf1tT2Tn9ymRyiaTIPAvY0MBsqSCSWOAujwB2tcv4ItkSib1W3g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

       1. 前后节点
       2. 头尾节点：头节点里有 head 和 tail 两个参数
       3. 链表长度：头节点里同时还有一个参数 len，和上边提到的 SDS 里类似

    3. 压缩列表：由于 ziplist 是连续紧凑存储，没有冗余空间，所以插入新的元素需要 realloc 扩展内存，所以如果 ziplist 占用空间太大，realloc 重新分配内存和拷贝的开销就会很大，所以 ziplist 不适合存储过多元素，也不适合存储过大的字符串

       1. ![图片](https://mmbiz.qpic.cn/mmbiz_png/g6hBZ0jzZb0Zb0XiaaR6bGaN80wicXIIP7WIOWyzXHCzPhr4bNJzH3QdYF8R4v27XuIWCqsg8PWGCGg39lTIe3LA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
       2. zlbytes：ziplist所占用的总内存字节数。
       3. Zltail：尾节点到起始位置的字节数。
       4. Zllen：总共包含的节点/内存块数。
       5. Entry：ziplist 保存的各个数据节点，这些数据点长度随意。
       6. Zlend：一个魔数 255，用来标记压缩列表的结束。

    4. 字典

    5. 跳跃表

12. 各种数据类型使用的底层结构

    1. String
       1. 当 int 编码保存的值不再是整数，或大小超过了long的范围时，自动转化为raw 
       2. 对于 embstr 编码，由于 Redis 没有对其编写任何的修改程序（embstr 是只读的），在对embstr对象进行修改时，都会先转化为raw再进行修改，因此，只要是修改embstr对象，修改后的对象一定是raw的，无论是否达到了44个字节。
       3. embstr 和 raw 编码方式最主要的区别是在内存分配的时候。embstr 编码是专门用于保存短字符串的一种优化编码方式，raw  编码会调用两次内存分配函数来分别创建 redisObject 结构和 sdshdr 结构，而 embstr  编码则通过调用一次内存分配函数来分配一块连续的空间，空间中依次包含redisObject和sdshdr两个结构
    2. Zset
       1. 当有序结合对象同时满足以下两个条件时，对象使用ziplist编码，否则使用skiplist编码
          1. 保存的元素数量小于128
          2. 保存的所有元素长度都小于64字节
       2. ziplist编码的有序集合对象使用压缩列表作为底层实现，每个集合元素使用两个紧挨在一起的压缩列表节点来保存，第一个节点保存元素的成员，第二个节点保存元素的分值。并且压缩列表内的集合元素按分值从小到大的顺序进行排列，小的放置在靠近表头的位置，大的放置在靠近表尾的位置
       3. skiplist编码的有序集合对象使用zset结构作为底层实现，一个zset结构同时包含一个字典和一个跳跃表

