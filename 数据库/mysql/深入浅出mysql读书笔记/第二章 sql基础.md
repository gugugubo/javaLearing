# 第2章 SQL 基础

## 2.1 (My)SQL 使用入门

### 2.2.1 SQL 分类

mysql主要可以分为三类

1. DDL（Data Definition Languages）语句：数据定义语言，这些语句定义了不同的数据段、数据库、表、列、索引等数据库对象的定义。常用的语句关键字主要包括 create、drop、alter等。（什么是数据段？[请点击](https://www.cnblogs.com/geaozhang/p/8610978.html)），DDL语句更多的被数据库管理员（DBA）所使用，一般的开发人员很少使用
   
2. DML（Data Manipulation Language）语句：数据操纵语句，用于添加、删除、更新和查询数据库记录，并检查数据完整性，常用的语句关键字主要包括 insert、delete、udpate 和select 等。DML和DDL最大的区别是DML只对表内的数据进行操作，而不涉及到表的定义和结构的修改。
3. DCL（Data Control Language）语句：数据控制语句，用于控制不同数据段直接的许可和访问级别的语句。这些语句定义了数据库、表、字段、用户的访问权限和安全级别。主要的语句关键字包括 grant、revoke 等。

### 2.2.2 DML 语句

#### 1. 插入语法

```sql
INSERT INTO tablename (field1,field2,……fieldn) VALUES(value1,value2,……valuesn);
```

例如，向表 emp 中插入以下记录：ename 为 zzx1，hiredate 为 2000-01-01，sal 为 2000，deptno为 1，命令执行如下：

```sql
mysql> insert into emp (ename,hiredate,sal,deptno) values('zzx1','2000-01-01','2000',1);
Query OK, 1 row affected (0.00 sec)
```

也可以不用指定字段名称，但是 values 后面的顺序应该和字段的排列顺序一致：

```sql
mysql> insert into emp values('lisa','2003-02-01','3000',2);
Query OK, 1 row affected (0.00 sec)
```

对于含可空字段、非空但是含有默认值的字段、自增字段，可以不用在 insert 后的字段列表里面出现，values 后面只写对应字段名称的 value，这些没写的字段可以自动设置为 NULL、默认值、自增的下一个数字，这样在某些情况下可以大大缩短 SQL 语句的复杂性。例如，只对表中的 ename 和 sal 字段显式插入值：

```sql
mysql> insert into emp (ename,sal) values('dony',1000);
Query OK, 1 row affected (0.00 sec)
#来查看一下实际插入值：
mysql> select * from emp;
+--------+------------+---------+--------+
| ename  | hiredate   | sal     | deptno |
+--------+------------+---------+--------+
| zzx    | 2000-01-01 | 100.00  | 1      |
| lisa   | 2003-02-01 | 400.00  | 2      |
| bjguan | 2004-04-02 | 100.00  | 1      |
| dony   | NULL       | 1000.00 | NULL   |
+--------+------------+---------+--------+
```


果然，设置为可空的两个字段都显示为 NULL。

#### 2. 更新记录

对于表里的记录值，可以通过 update 命令进行更改，语法如下：

```sql
UPDATE tablename SET field1=value1，field2.=value2，……fieldn=valuen [WHERE CONDITION]
```

例如，将表 emp 中 ename 为“lisa”的薪水（sal）从 3000 更改为 4000：

```mysql
mysql> update emp set sal=4000 where ename='lisa';
Query OK, 1 row affected (0.00 sec)
Rows matched: 1 Changed: 1 Warnings: 0
```

在 MySQL 中，update 命令可以同时更新多个表中数据，语法如下：

```mysql
UPDATE t1,t2…tn set t1.field1=expr1,tn.fieldn=exprn [WHERE CONDITION]
```

#### 3. 删除记录

如果记录不再需要，可以用delete命令进行删除，语法如下：

```sql
DELETE FROM tablename [WHERE CONDITION]
```

例如，在emp中将ename为‘dony’的记录全部删除，命令如下：

```sql
mysql> delete from emp where ename='dony';Query OK, 1 row affected (0.00 sec)
```

在MySQL中可以一次删除多个表的数据，语法如下：

```sql
DELETE t1,t2...tn FROM t1,t2...tn [WHERE CONDITION]
```

#### 4. 查询记录

数据插入到数据库中后，就可以用SELECT命令进行各种各样的查询，使得输出的结果符合我们的要求。由于SELECT的语法很复杂，所有这里只介绍最基本的语法：

```sql
SELECT * FROM tablename [WHERE CONDITION]
```

##### **聚合**

很多情况下我们需要进行一些汇总操作比如统计整个公司的人数或者统计每个部门的人数，这个时就要用到SQL的聚合操作。聚合操作的语法如下：

```mysql
SELECT [field1,field2,......fieldn] fun_name 
FROM tablename
[WHERE where_contition]
[GROUP BY field1,field2,......fieldn[WITH ROLLUP]]  # 注意这里的这个"]"符号
[HAVING where_contition]
```

对其参数进行以下说明。

1. fun_name表示要做的聚合操作，也就是聚合函数，常用的有sum（求和）、count(*)（记录数）、max（最大值）、min（最小值）。
2. GROUP BY关键字表示要进行分类聚合的字段，比如要按照部门分类统计员工数量，部门就应该写在group by后面。
3. WITH ROLLUP是可选语法，表明是否对分类聚合后的结果进行再汇总。
4. HAVING关键字表示对分类后的结果再进行条件的过滤。

> 注意：having和where的区别在于having是对聚合后的结果进行条件的过滤，而where是在聚合前就对记录进行过滤，如果逻辑允许，我们尽可能用where先过滤记录，这样因为结果集减小，将对聚合的效率大大提高，最后再根据逻辑看是否用having进行再过滤。

------



1. 既要统计各部门人数，又要统计总人数：

   1. ```sql
      mysql> select deptno,count(1) from emp group by deptno with rollup;
      +--------+----------+
      | deptno | count(1) |
      +--------+----------+
      | 1      | 2        |
      | 2      | 1        |
      | 4      | 1        |
      | NULL   | 4        |
      +--------+----------+
      4 rows in set (0.00 sec)	
      ```

2. 统计人数大于 1 人的部门：

   1. ```sql
      mysql> select deptno,count(1) from emp group by deptno having count(1)>1;
      +--------+----------+
      | deptno | count(1) |
      +--------+----------+
      | 1      | 2        |
      +--------+----------+
      1 row in set (0.00 sec)
      ```

##### 表连接

当需要同时显示多个表中的字段时，就可以用表连接来实现这样的功能。从大类上分，表连接分为内连接和外连接，它们之间的最主要区别是內连接仅选出两张表中互相匹配的记录，而外连接会选出其他不匹配的记录。我们最常用的是内连接。

例如，查询出所有雇员的名字和所在部门名称，因为雇员名称和部门分别存放在表 emp 和
dept 中，因此，需要使用表连接来进行查询：

```sql
mysql> select * from emp;
+--------+------------+---------+--------+
| ename  | hiredate   | sal     | deptno |
+--------+------------+---------+--------+
| zzx    | 2000-01-01 | 2000.00 | 1      |
| lisa   | 2003-02-01 | 4000.00 | 2      |
| bjguan | 2004-04-02 | 5000.00 | 1      |
| bzshen | 2005-04-01 | 4000.00 | 3      |
+--------+------------+---------+--------+
4 rows in set (0.00 sec)
mysql> select * from dept;
+--------+----------+
| deptno | deptname |
+--------+----------+
| 1      | tech     |
| 2      | sale     |
| 3      | hr       |
+--------+----------+
3 rows in set (0.00 sec)
mysql> select ename,deptname from emp,dept where emp.deptno=dept.deptno;
+--------+----------+
| ename  | deptname |
+--------+----------+
| zzx    | tech     |
| lisa   | sale     |
| bjguan | tech     |
| bzshen | hr       |
+--------+----------+
4 rows in set (0.00 sec)

```

外连接有分为左连接和右连接，具体定义如下。

- 左连接：包含所有的左边表中的记录甚至是右边表中没有和它匹配的记录
- 右连接：包含所有的右边表中的记录甚至是左边表中没有和它匹配的记录

例如，查询 emp 中所有用户名和所在部门名称：

```sql
ysql> select * from emp;
+--------+------------+---------+--------+
| ename  | hiredate   | sal     | deptno |
+--------+------------+---------+--------+
| zzx    | 2000-01-01 | 2000.00 | 1      |
| lisa   | 2003-02-01 | 4000.00 | 2      |
| bjguan | 2004-04-02 | 5000.00 | 1      |
| bzshen | 2005-04-01 | 4000.00 | 3      |
| dony | 2005-02-05   | 2000.00 | 4      |
+--------+------------+---------+--------+
5 rows in set (0.00 sec)
mysql> select * from dept;
+--------+----------+
| deptno | deptname |
+--------+----------+
| 1      | tech     |
| 2      | sale     |
| 3      | hr       |
+--------+----------+
3 rows in set (0.00 sec)
mysql> select ename,deptname from emp left join dept on emp.deptno=dept.deptno;
+--------+----------+
| ename  | deptname |
+--------+----------+
| zzx    | tech     |
| lisa   | sale     |
| bjguan | tech     |
| bzshen | hr       |
| dony   |          |
+--------+----------+
5 rows in set (0.00 sec)

```

比较这个查询和上例中的查询，都是查询用户名和部门名，两者的区别在于本例中列出了所有的用户名，即使有的用户名（dony）并不存在合法的部门名称（部门号为 4，在 dept 中没有这个部门）；而上例中仅仅列出了存在合法部门的用户名和部门名称。右连接和左连接类似，两者之间可以互相转化

##### 子查询

某些情况下，当我们查询的时候，需要的条件是另外一个 select 语句的结果，这个时候，就要用到子查询。用于子查询的关键字主要包括 in、not in、=、!=、exists、not exists 等。
例如，从 emp 表中查询出所有部门在 dept 表中的所有记录：

```sql
mysql> select * from emp;
+--------+------------+---------+--------+
| ename  | hiredate   | sal     | deptno |
+--------+------------+---------+--------+
| zzx    | 2000-01-01 | 2000.00 | 1 |
| lisa   | 2003-02-01 | 4000.00 | 2 |
| bjguan | 2004-04-02 | 5000.00 | 1 |
| bzshen | 2005-04-01 | 4000.00 | 3 |
| dony   | 2005-02-05 | 2000.00 | 4 |
+--------+------------+---------+--------+
5 rows in set (0.00 sec)
mysql> select * from dept;
+--------+----------+
| deptno | deptname |
+--------+----------+
| 1      | tech     |
| 2      | sale     |
| 3      | hr       |
| 5      | fin      |
+--------+----------+
4 rows in set (0.00 sec)
mysql> select * from emp where deptno in(select deptno from dept);
+--------+------------+---------+--------+
| ename  | hiredate   | sal     | deptno |
+--------+------------+---------+--------+
| zzx    | 2000-01-01 | 2000.00 | 1      |
| lisa   | 2003-02-01 | 4000.00 | 2      |
| bjguan | 2004-04-02 | 5000.00 | 1      |
| bzshen | 2005-04-01 | 4000.00 | 3      |
+--------+------------+---------+--------+
4 rows in set (0.00 sec)

```

> 注意：子查询和表连接之间的转换主要应用在两个方面：
>
> - MySQL 4.1 以前的版本不支持子查询，需要用表连接来实现子查询的功能
> - 表连接在很多情况下用于优化子查询

##### 记录联合

我们经常会碰到这样的应用，将两个表的数据按照一定的查询条件查询出来后，将结果合并到一起显示出来，这个时候，就需要用 union 和 union all 关键字来实现这样的功能，具体语法如下：

```sql
SELECT * FROM t1
UNION|UNION ALL
SELECT * FROM t2
……
UNION|UNION ALL
SELECT * FROM tn;

```

UNION 和 UNION ALL 的主要区别是 UNION ALL 是把结果集直接合并在一起，而 UNION 是将UNION ALL 后的结果进行一次 DISTINCT，去除重复记录后的结果。来看下面例子，将 emp 和 dept 表中的部门编号的集合显示出来：

```sql
mysql> select * from emp;
+--------+------------+---------+--------+
| ename  | hiredate   | sal     | deptno |
+--------+------------+---------+--------+
| zzx    | 2000-01-01 | 100.00  | 1      |
| lisa   | 2003-02-01 | 400.00  | 2      |
| bjguan | 2004-04-02 | 100.00  | 1      |
| dony   | 2005-02-05 | 2000.00 | 4      |
+--------+------------+---------+--------+
4 rows in set (0.00 sec)
mysql> select * from dept;
+--------+----------+
| deptno | deptname |
+--------+----------+
| 1      | tech     |
| 2      | sale     |
| 5      | fin      |
+--------+----------+
3 rows in set (0.00 sec)
mysql> select deptno from emp 
-> union all
-> select deptno from dept;
+--------+
| deptno |
+--------+
| 1      |
| 2      |
| 1      |
| 4      |
| 1      |
| 2      |
| 5      |
+--------+
```

如果希望将结果去掉重复记录后显示：

```sql
mysql> select deptno from emp 
 
-> union
 
-> select deptno from dept;
+--------+
| deptno |
+--------+
| 1      |
| 2      |
| 4      |
| 5      |
+--------+
4 rows in set (0.00 sec)

```

### 2.2.4 DCL 语句

DCL 语句主要是 DBA 用来管理系统中的对象权限时所使用，一般的开发人员很少使用。下面通过一个例子来简单说明一下。
创建一个数据库用户 z1，具有对 sakila 数据库中所有表的 SELECT/INSERT 权限：

```sql
mysql> grant select,insert on sakila.* to 'z1'@'localhost' identified by '123';
Query OK, 0 rows affected (0.00 sec)
mysql> exit
Bye
[mysql@db3 ~]$ mysql -uz1 -p123
Welcome to the MySQL monitor. Commands end with ; or \g.
Your MySQL connection id is 21671 to server version: 5.1.9-beta-log
Type 'help;' or '\h' for help. Type '\c' to clear the buffer.
mysql> use sakila
Database changed
mysql> insert into emp values('bzshen','2005-04-01',3000,'3');
Query OK, 1 row affected (0.04 sec)

```

由于权限变更，需要将 z1 的权限变更，收回 INSERT，只能对数据进行 SELECT 操作：

```sql
[mysql@db3 ~]$ mysql -uroot
Welcome to the MySQL monitor. Commands end with ; or \g.
Your MySQL connection id is 21757 to server version: 5.1.9-beta-log
Type 'help;' or '\h' for help. Type '\c' to clear the buffer.
mysql> revoke insert on sakila.* from 'z1'@'localhost';
Query OK, 0 rows affected (0.00 sec)
mysql> exit
Bye

```

用户 z1 重新登录后执行前面语句：

```sql
[mysql@db3 ~]$ mysql -uz1 -p123
Welcome to the MySQL monitor. Commands end with ; or \g.
Your MySQL connection id is 21763 to server version: 5.1.9-beta-log
Type 'help;' or '\h' for help. Type '\c' to clear the buffer.
mysql> use sakila
Database changed
mysql> insert into emp values('bzshen','2005-04-01',3000,'3');
ERROR 1142 (42000): INSERT command denied to user 'z1'@'localhost' for table 'emp'

```

以上例子中的 grant 和 revoke 分别授出和收回了用户 z1 的部分权限，达到了我们的目的。关于权限的更多内容，将会在第 4 篇中详细介绍。