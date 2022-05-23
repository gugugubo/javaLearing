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

建立索引



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



## 18.3 两个简单实用的优化方法

### 18.3.1 定期分析表和检查表

分析表的语法如下：

```sql
ANALYZE [LOCAL | NO_WRITE_TO_BINLOG] TABLE tbl_name [, tbl_name] ... 
```

本语句用于分析和存储表的关键字分布，分析的结果将可以使得系统得到准确的统计信息，使得 SQL 能够生成正确的执行计划。如果用户感觉实际执行计划并不是预期的执行计划，执行一次分析表可能会解决问题。在分析期间，使用一个读取锁定对表进行锁定。这对于 MyISAM, BDB 和 InnoDB 表有作用。对于 MyISAM 表，本语句与使用 myisamchk -a 相当.
下例中对表 sales 做了表分析：

```sql
mysql> analyze table sales;
+--------------+---------+----------+----------+
| Table        | Op      | Msg_type | Msg_text |
+--------------+---------+----------+----------+
| sakila.sales | analyze | status   | OK       |
+--------------+---------+----------+----------+
1 row in set (0.00 sec)

```

检查表的语法如下：

```sql
CHECK TABLE tbl_name [, tbl_name] ... [option] ... option = {QUICK | FAST | MEDIUM | EXTENDED 
| CHANGED} 

```

检查表的作用是检查一个或多个表是否有错误。CHECK TABLE对MyISAM 和InnoDB表有作用。对于 MyISAM 表，关键字统计数据被更新，例如：

```sql
mysql> check table sales;
+--------------+-------+----------+----------+
| Table        | Op    | Msg_type | Msg_text |
+--------------+-------+----------+----------+
| sakila.sales | check | status   | OK       |
+--------------+-------+----------+----------+
1 row in set (0.00 sec)

```

CHECK TABLE 也可以检查视图是否有错误，比如在视图定义中被引用的表已不存在，举例如下。

（1）首先我们创建一个视图。

```sql
mysql> create view sales_view3 as select * from sales3;
Query OK, 0 rows affected (0.00 sec)
```

（2）然后 CHECK 一下该视图，发现没有问题。

```sql
mysql> check table sales_view3;
+--------------------+-------+----------+----------+
| Table              | Op    | Msg_type | Msg_text |
+--------------------+-------+----------+----------+
| sakila.sales_view3 | check | status   | OK       |
+--------------------+-------+----------+----------+
1 row in set (0.00 sec)

```



（3）现在删除掉视图依赖的表。

```sql
mysql> drop table sales3;
Query OK, 0 rows affected (0.00 sec)
```

（4）再来 CHECK 一下刚才的视图，发现报错了。

```sql
mysql> check table sales_view3\G;
*************************** 1. row ***************************
Table: sakila.sales_view3
Op: check
Msg_type: error
Msg_text: View 'sakila.sales_view3' references invalid table(s) or column(s) or function(s) 
or definer/invoker of view lack rights to use them
1 row in set (0.00 sec)

```

### 18.3.2 定期优化表

优化表的语法如下：

```sql
OPTIMIZE [LOCAL | NO_WRITE_TO_BINLOG] TABLE tbl_name [, tbl_name] ...
```

如果已经删除了表的一大部分，或者如果已经对含有可变长度行的表（含有 VARCHAR、BLOB 或 TEXT 列的表）进行了很多更改，则应使用 OPTIMIZE TABLE 命令来进行表优化。这个命令可以将表中的空间碎片进行合并，并且可以消除由于删除或者更新造成的空间浪费，但OPTIMIZE TABLE 命令只对 MyISAM、BDB 和 InnoDB 表起作用。
以下例子显示了优化表 sales 的过程：

```sql
mysql> optimize table sales;
+--------------+----------+----------+----------+
| Table        | Op       | Msg_type | Msg_text |
+--------------+----------+----------+----------+
| sakila.sales | optimize | status   | OK       |
+--------------+----------+----------+----------+
1 row in set (0.00 sec)

```

>    注意：ANALYZE、CHECK、OPTIMIZE 执行期间将对表进行锁定，因此一定注意要在数据库不
> 繁忙的时候执行相关的操作。





## 18.4 常用 SQL 的优化

### 18.4.2 优化 INSERT 语句

当进行数据 INSERT 的时候，可以考虑采用以下几种优化方式。
 如果同时从同一客户插入很多行，尽量使用多个值表的 INSERT 语句，这种方式将大大缩减客户端与数据库之间的连接、关闭等消耗，使得效率比分开执行的单个 INSERT 语句快(在一些情况中几倍)。下面是一次插入多值的一个例子：
insert into test values(1,2),(1,3),(1,4)…
 如果从不同客户插入很多行，能通过使用 INSERT DELAYED 语句得到更高的速度。DELAYED 的含义是让 INSERT 语句马上执行，其实数据都被放在内存的队列中，并没有真正写入磁盘，这比每条语句分别插入要快的多；LOW_PRIORITY 刚好相反，在所有其他用户对表的读写完后才进行插入；

### 18.4.3 优化 GROUP BY 语句

默认情况下，MySQL 就会对所有 GROUP BY col1，col2....的字段进行排序。这与在查询中指定ORDER BY col1，col2...类似。因此，如果显式包括一个包含相同的列的 ORDER BY 子句，则对 MySQL 的实际执行性能没有什么影响。如果查询包括 GROUP BY 但用户想要避免排序结果的消耗，则可以指定 ORDER BY NULL禁止排序，如下面的例子：

```sql
mysql> explain select id,sum(moneys) from sales2 group by id\G;
# 此处的id是没有设置索引的，因为如果设置索引默认就是已经排好序的了
*************************** 1. row ***************************
id: 1
select_type: SIMPLE
table: sales2
type: ALL
possible_keys: NULL
key: NULL
key_len: NULL
ref: NULL
rows: 1000
Extra: Using temporary; Using filesort
1 row in set (0.00 sec)
mysql> explain select id,sum(moneys) from sales2 group by id order by null\G;
*************************** 1. row ***************************
id: 1
select_type: SIMPLE
table: sales2
type: ALL
possible_keys: NULL
key: NULL
key_len: NULL
ref: NULL
rows: 1000
Extra: Using temporary
1 row in set (0.00 sec)

```

从上面的例子可以看出第一个SQL语句需要进行“filesort”，而第二个SQL由于ORDER BY NULL不需要进行“filesort”，而 filesort 往往非常耗费时间。

### 18.4.4 优化 ORDER BY 语句：

在某些情况中，MySQL 可以使用一个索引来满足 ORDER BY 子句，而不需要额外的排序。WHERE 条件和 ORDER BY 使用相同的索引，并且 ORDER BY 的顺序和索引顺序相同，并且ORDER BY 的字段都是升序或者都是降序。
例如，下列 SQL 可以使用索引。

```sql
SELECT * FROM t1 ORDER BY key_part1,key_part2,... ;
SELECT * FROM t1 WHERE key_part1=1 ORDER BY key_part1 DESC, key_part2 DESC;
SELECT * FROM t1 ORDER BY key_part1 DESC, key_part2 DESC;
```

但是在以下几种情况下则不使用索引：

```sql
SELECT * FROM t1 ORDER BY key_part1 DESC, key_part2 ASC；
--order by 的字段混合 ASC 和 DESC
SELECT * FROM t1 WHERE key2=constant ORDER BY key1；
--用于查询行的关键字与 ORDER BY 中所使用的不相同
SELECT * FROM t1 ORDER BY key1, key2；
--对不同的关键字使用 ORDER BY：
```

### 18.4.5 优化嵌套查询



### 18.4.6 MySQL 如何优化 OR 条件





### 18.4.7 使用 SQL 提示

SQL 提示（SQL HINT）是优化数据库的一个重要手段，简单来说就是在 SQL 语句中加入一些人为的提示来达到优化操作的目的。下面是一个使用 SQL 提示的例子：`SELECT SQL_BUFFER_RESULTS * FROM...`这个语句将强制 MySQL 生成一个临时结果集。只要临时结果集生成后，所有表上的锁定均被释放。这能在遇到表锁定问题时或要花很长时间将结果传给客户端时有所帮助，因为可以尽快释放锁资源。
下面是一些在 MySQL 中常用的 SQL 提示。

#### 1．USE INDEX

在查询语句中表名的后面，添加 USE INDEX 来提供希望 MySQL 去参考的索引列表，就可以让 MySQL 不再考虑其他可用的索引。

```sql
mysql> explain select * from sales2 use index (ind_sales2_id) where id = 3\G;
*************************** 1. row ***************************
id: 1
select_type: SIMPLE
table: sales2
type: ref
possible_keys: ind_sales2_id
key: ind_sales2_id
key_len: 5
ref: const
rows: 1
Extra: Using where
1 row in set (0.00 sec).
```

#### 2．IGNORE INDEX

如果用户只是单纯地想让 MySQL 忽略一个或者多个索引，则可以使用 IGNORE INDEX 作为 HINT。同样是上面的例子，这次来看一下查询过程忽略索引 ind_sales2_id 的情况：

```sql
mysql> explain select * from sales2 ignore index (ind_sales2_id) where id = 3\G;
*************************** 1. row *************************** 
id: 1 
select_type: SIMPLE 
table: sales2 
type: ALL
possible_keys: NULL 
key: NULL 
key_len: NULL
ref: NULL 
rows: 1000 
Extra: Using where
1 row in set (0.00 sec).
```

从执行计划可以看出，系统忽略了指定的索引，而使用了全表扫描。

#### 3．FORCE INDEX

为强制 MySQL 使用一个特定的索引，可在查询中使用 FORCE INDEX 作为 HINT。例如，当不强制使用索引的时候，因为 id 的值都是大于 0 的，因此 MySQL 会默认进行全表扫描，而不使用索引，如下所示：

```sql
mysql> explain select * from sales2 where id > 0 \G;
*************************** 1. row ***************************
id: 1
select_type: SIMPLE
table: sales2
type: ALL
possible_keys: ind_sales2_id
key: NULL 
key_len: NULL
ref: NULL
rows: 1000
Extra: Using where
1 row in set (0.00 sec)
```

但是，当使用 FORCE INDEX 进行提示时，即便使用索引的效率不是最高，MySQL 还是选择使用了索引，这是 MySQL 留给用户的一个自行选择执行计划的权力。加入 FORCE INDEX 提示后再次执行上面的 SQL：

```sql
mysql> explain select * from sales2 force index (ind_sales2_id) where id > 0 
\G;
*************************** 1. row *************************** 
id: 1
select_type: SIMPLE 
table: sales2 
type: range
possible_keys: ind_sales2_id 
key: ind_sales2_id  
key_len: 5
ref: NULL
rows: 1000
Extra: Using where
1 row in set (0.00 sec).
```

果然，执行计划中使用了 FORCE INDEX 后的索引。