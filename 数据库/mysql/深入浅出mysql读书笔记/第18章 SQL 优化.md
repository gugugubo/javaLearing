# 第18章 SQL 优化

## 18.1 优化 SQL 语句的一般步骤

### 18.1.1 通过 show status 命令了解各种 SQL 的执行频率

MySQL 客户端连接成功后，通过 show [session|global]status 命令可以提供服务器状态信息，也可以在操作系统上使用 [mysqladmin extended-status 命令](https://www.cnblogs.com/workdsz/articles/9455695.html)获得这些消息。show[session|global] status 可以根据需要加上参数“session”或者“global”来显示 session 级（当前连接）的统计结果和 global 级（自数据库上次启动至今）的统计结果。如果不写，默认使用参数是“session”。

下面的命令显示了当前 session 中所有统计参数的值：

```sql
mysql> show  status like 'Com_%';
+-----------------------------+-------+
| Variable_name               | Value |
+-----------------------------+-------+
| Com_admin_commands          | 0     |
| Com_assign_to_keycache      | 0     |
| Com_alter_db                | 0     |
| Com_alter_db_upgrade        | 0     |
| Com_alter_event             | 0     |
| Com_alter_function          | 0     |
| Com_alter_instance          | 0     |
| Com_alter_procedure         | 0     |
| Com_alter_server            | 0     |
| Com_alter_table             | 0     |
.......
```

Com_xxx 表示每个 xxx 语句执行的次数，我们通常比较关心的是以下几个统计参数。
 Com_select：执行 select 操作的次数，一次查询只累加 1。
 Com_insert：执行 INSERT 操作的次数，对于批量插入的 INSERT 操作，只累加一次。
 Com_update：执行 UPDATE 操作的次数。
 Com_delete：执行 DELETE 操作的次数。
上面这些参数对于所有存储引擎的表操作都会进行累计。下面这几个参数只是针对InnoDB 存储引擎的，累加的算法也略有不同。
 Innodb_rows_read：select 查询返回的行数。
 Innodb_rows_inserted：执行 INSERT 操作插入的行数。
 Innodb_rows_updated：执行 UPDATE 操作更新的行数。
 Innodb_rows_deleted：执行 DELETE 操作删除的行数。
通过以上几个参数，可以很容易地了解当前数据库的应用是以插入更新为主还是以查询操作为主，以及各种类型的 SQL 大致的执行比例是多少。对于更新操作的计数，是对执行次数的计数，不论提交还是回滚都会进行累加。对于事务型的应用，通过 Com_commit 和 Com_rollback 可以了解事务提交和回滚的情况，对于回滚操作非常频繁的数据库，可能意味着应用编写存在问题。此外，以下几个参数便于用户了解数据库的基本情况。
 Connections：试图连接 MySQL 服务器的次数。
 Uptime：服务器工作时间。
 Slow_queries：慢查询的次数。

### 18.1.2 定位执行效率较低的 SQL 语句

可以通过以下两种方式定位执行效率较低的 SQL 语句。
 通过慢查询日志定位那些执行效率较低的 SQL 语句，用`--log-slow-queries[=file_name]`选项启动时，mysqld 写一个包含所有执行时间超过 `long_query_time` 秒的 SQL 语句的日志文件。
 慢查询日志在查询结束以后才纪录，所以在应用反映执行效率出现问题的时候查询慢查询日志并不能定位问题，可以使用`show processlist`命令查看当前MySQL在进行的线程，包括线程的状态、是否锁表等，可以实时地查看 SQL 的执行情况，同时对一些锁表操作进行优化。

### 18.1.3 通过 EXPLAIN 分析低效 SQL 的执行计划

通过以上步骤查询到效率低的 SQL 语句后，可以通过 EXPLAIN 或者 DESC 命令获取 MySQL如何执行 SELECT 语句的信息，包括在 SELECT 语句执行过程中表如何连接和连接的顺序。



### 18.1.4 确定问题并采取相应的优化措施

### 

## 18.2 索引问题

### 18.2.2 MySQL 如何使用索引

#### 1．使用索引

（1）对于创建的多列索引，只要查询的条件中用到了最左边的列，索引一般就会被使用，
（2）对于使用 like 的查询，后面如果是常量并且％号不在第一个字符，索引才可能会被使用

（3）如果对大的文本进行搜索，使用全文索引而不用使用 like ‘%…%’。

（4）如果列名是索引，使用 column_name is null 将使用索引。

#### 2．存在索引但不使用索引

（1） 如果 MySQL 估计使用索引比全表扫描更慢，则不使用索引。例如如果列key_part1 均匀分布在 1 和 100 之间，下列查询中使用索引就不是很好：

```sql
SELECT * FROM table_name where key_part1 > 1 and key_part1 < 90;
```

（2） 如果使用 MEMORY/HEAP 表并且 where 条件中不使用“=”进行索引列，那么不会用到索引。heap 表只有在“=”的条件下才会使用索引。

（3） 用 or 分割开的条件，如果 or 前的条件中的列有索引，而后面的列中没有索引，那么涉及到的索引都不会被用到

（4） 如果列类型是字符串，那么一定记得在 where 条件中把字符常量值(如where id = '2323')用引号引起来，否则的话即便这个列上有索引，MySQL 也不会用到的，因为，MySQL 默认把输入的常量值进行转换以后才进行检索。

### 18.2.3 查看索引使用情况

如果索引正在工作，Handler_read_key 的值将很高，这个值代表了一个行被索引值读的次数，很低的值表明增加索引得到的性能改善不高，因为索引并不经常使用。Handler_read_rnd_next 的值高则意味着查询运行低效，并且应该建立索引补救。这个值表明在进行数据文件扫描时，从数据文件里取数据的次数。如果正进行大量的表扫描，Handler_read_rnd_next 的值较高，则通常说明表索引不正确或写入的查询没有利用索引

```sql
mysql> show status like 'Handler_read%';
+-----------------------+-------+
| Variable_name         | Value |
+-----------------------+-------+
| Handler_read_first    | 0     |
| Handler_read_key      | 5     |
| Handler_read_next     | 0     |
| Handler_read_prev     | 0     |
| Handler_read_rnd      | 0     |
| Handler_read_rnd_next | 2055  |
+-----------------------+-------+
6 rows in set (0.00 sec)
```


从上面的例子中可以看出，目前使用的 MySQL 数据库的索引情况并不理想。

