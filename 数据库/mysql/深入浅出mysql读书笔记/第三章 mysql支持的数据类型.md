

# 第3章 MySQL支持的数据类型

## 3.1 数值类型

MySQL 支持所有标准 SQL 中的数值类型，其中包括严格数值类型（INTEGER、SMALLINT、DECIMAL （即NUMERIC）），以及近似数值数据类型（FLOAT、REAL 和 DOUBLE PRECISION）表 3-1 中列出了 MySQL 5.0 中支持的所有数值类型，其中 INT 是 INTEGER 的同名词，DEC 是 DECIMAL 的同名词。

![1584924399447](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323084639-514541.png)

在整数类型中，按照取值范围和存储方式不同，分为 tinyint、smallint、mediumint、int、bigint 这 5 个类型。如果超出类型范围的操作，会发生“Out of range”错误提示。为了避免此类问题发生，在选择数据类型时要根据应用的实际情况确定其取值范围，最后根据确定的结果慎重选择数据类型。
对于整型数据，MySQL 还支持在类型名称后面的小括号内指定显示宽度，例如 int(5)表示当数值宽度小于 5 位的时候在数字前面填满宽度，如果不显示指定宽度则默认为 int(11)。一般配合 zerofill 使用，顾名思义，zerofill 就是用“0”填充的意思，也就是在数字位数不够的空间用字符“0”填满（如果一个列指定为 zerofill，则 MySQL 自动为该列添加 UNSIGNED 属性）

### zerofill参数

**分别修改 id1 和 id2 的字段类型，加入 zerofill 参数：**

```sql
mysql> alter table t1 modify id1 int zerofill;
Query OK, 1 row affected (0.04 sec)
Records: 1 Duplicates: 0 Warnings: 0
mysql> alter table t1 modify id2 int(5) zerofill;
Query OK, 1 row affected (0.03 sec)
Records: 1 Duplicates: 0 Warnings: 0
mysql> select * from t1;
+------------+-------+
| id1        | id2   |
+------------+-------+
| 0000000001 | 00001 | 
+------------+-------+
1 row in set (0.00 sec)

```

可以发现，在数值前面用字符“0”填充了剩余的宽度。大家可能会有所疑问，设置了宽度限制后，如果插入大于宽度限制的值，会不会截断或者插不进去报错？答案是肯定的：[不会对插入的数据有任何影响，还是按照类型的实际精度进行保存](https://www.cnblogs.com/EasonJim/p/7737029.html)，这时，宽度格式实际已经没有意义，左边不会再填充任何的“0”字符。下面在表 t1 的字段 id1 中插入数值 1，id2 中插入数值 1111111，位数为 7，大于 id2 的显示位数 5，再观察一下测试结果：很显然，如上面所说，id2 中显示了正确的数值，并没有受宽度限制影响。

```sql
mysql> insert into t1 values(1,1111111);
Query OK, 1 row affected (0.00 sec)
mysql> select * from t1;
+------------+---------+
| id1        | id2     |
+------------+---------+
| 0000000001 | 00001   | 
| 0000000001 | 1111111 | 
+------------+---------+
2 rows in set (0.00 sec)

```

### AUTO_INCREMENT属性

整数类型还有一个属性：AUTO_INCREMENT。在需要产生唯一标识符或顺序值时，可利用此属性，这个属性只用于整数类型。AUTO_INCREMENT 值一般从 1 开始，每行增加 1。在插入 NULL 到一个 AUTO_INCREMENT 列时，MySQL 插入一个比该列中当前最大值大 1 的值。一个表中最多只能有一个AUTO_INCREMENT列。对于任何想要使用AUTO_INCREMENT 的列，应该定义为 NOT NULL，并定义为 [PRIMARY KEY(主键约束) 或定义为 UNIQUE 键(除null外的唯一约束)](https://blog.csdn.net/zm2714/article/details/8482625?depth_1-utm_source=distribute.pc_relevant.none-task&utm_source=distribute.pc_relevant.none-task)。例如，可按下列任何一种方式定义 AUTO_INCREMENT 列：

```sql
CREATE TABLE AI (ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY);
CREATE TABLE AI(ID INT AUTO_INCREMENT NOT NULL ,PRIMARY KEY(ID));
CREATE TABLE AI (ID INT AUTO_INCREMENT NOT NULL ,UNIQUE(ID));
```

### 小数的表示

对于小数的表示，MySQL 分为两种方式：浮点数和定点数。浮点数包括 float（单精度）和 double（双精度），而定点数则只有 decimal 一种表示。定点数在 MySQL 内部以字符串形式存放，比浮点数更精确，适合用来表示货币等精度高的数据。

浮点数和定点数都可以用类型名称后加“(M,D)”的方式来进行表示，“(M,D)”表示该值一共显示 M 位数字（整数位+小数位），其中 D 位位于小数点后面，M 和 D 又称为精度和标度。例如，定义为 float(7,4)的一个列可以显示为-999.9999。MySQL 保存值时进行四舍五入，因此如果在 float(7,4)列内插入 999.00009，近似结果是 999.0001。值得注意的是，浮点
数后面跟“(M,D)”的用法是非标准用法，如果要用于数据库的迁移，则最好不要这么使用。float 和 double 在不指定精度时，默认会按照实际的精度（由实际的硬件和操作系统决定）来显示，而 decimal 在不指定精度时，默认的整数位为 10，默认的小数位为 0。下面通过一个例子来比较 float、double 和 decimal 三者之间的不同。
（1）创建测试表，分别将 id1、id2、id3 字段设置为 float(5,2)、double(5,2)、decimal(5,2)。

```sql
CREATE TABLE `t1` (
`id1` float(5,2) default NULL,
`id2` double(5,2) default NULL,
`id3` decimal(5,2) default NULL
)
```

（2）往 id1、id2 和 id3 这 3 个字段中插入数据 1.23。

```sql
mysql> insert into t1 values(1.23,1.23,1.23);
Query OK, 1 row affected (0.00 sec)
mysql> 
mysql> select * from t1;
+------+------+------+
| id1  | id2  | id3  |
+------+------+------+
| 1.23 | 1.23 | 1.23 | 
+------+------+------+
1 row in set (0.00 sec)
```

可以发现，数据都正确地插入了表 t1。
（3）再向 id1 和 id2 字段中插入数据 1.234，而 id3 字段中仍然插入 1.23。

```sql
mysql> insert into t1 values(1.234,1.234,1.23);
Query OK, 1 row affected (0.00 sec)
mysql> select * from t1;
+------+------+------+
| id1  | id2  | id3  |
+------+------+------+
| 1.23 | 1.23 | 1.23 | 
| 1.23 | 1.23 | 1.23 | 
+------+------+------+
2 rows in set (0.00 sec)
```

可以发现，id1、id2、id3 都插入了表 t1，但是 id1 和 id2 由于标度的限制，舍去了最后一位，数据变为了 1.23。

（4）同时向 id1、id2、id3 字段中都插入数据 1.234。

```sql
mysql> insert into t1 values(1.234,1.234,1.234);
Query OK, 1 row affected, 1 warning (0.00 sec)
mysql> show warnings;
+-------+------+------------------------------------------+
| Level | Code | Message                                  |
+-------+------+------------------------------------------+
| Note | 1265 | Data truncated for column 'id3' at row 1  | 
+-------+------+------------------------------------------+
1 row in set (0.00 sec)
mysql> select * from t1;
+------+------+------+
| id1  | id2  | id3  |
+------+------+------+
| 1.23 | 1.23 | 1.23 | 
| 1.23 | 1.23 | 1.23 | 
| 1.23 | 1.23 | 1.23 | 
+------+------+------+
3 rows in set (0.00 sec)

```

此时发现，虽然数据都插入进去，但是系统出现了一个 warning，报告 id3 被截断。如果是在传统的 SQLMode（第 16 章将会详细介绍 SQLMode）下，这条记录是无法插入的。

（5）将 id1、id2、id3 字段的精度和标度全部去掉，再次插入数据 1.23。

```sql
mysql> alter table t1 modify id1 float;
Query OK, 3 rows affected (0.03 sec)
Records: 3 Duplicates: 0 Warnings: 0
mysql> alter table t1 modify id2 double;
Query OK, 3 rows affected (0.04 sec)
Records: 3 Duplicates: 0 Warnings: 0
mysql> alter table t1 modify id3 decimal;
Query OK, 3 rows affected, 3 warnings (0.02 sec)
Records: 3 Duplicates: 0 Warnings: 0
mysql> desc t1;
+-------+---------------+------+-----+---------+-------+
| Field | Type          | Null | Key | Default | Extra |
+-------+---------------+------+-----+---------+-------+
| id1   | float         | YES  |     | NULL    |       | 
| id2   | double        | YES  |     | NULL    |       | 
| id3   | decimal(10,0) | YES  |     | NULL    |       | 
+-------+---------------+------+-----+---------+-------+
3 rows in set (0.00 sec)
mysql> insert into t1 values(1.234,1.234,1.234);
Query OK, 1 row affected, 1 warning (0.00 sec)
mysql> show warnings;
+-------+------+------------------------------------------+
| Level | Code | Message                                  |
+-------+------+------------------------------------------+
| Note  | 1265 | Data truncated for column 'id3' at row 1 | 
+-------+------+------------------------------------------+
1 row in set (0.00 sec)
mysql> select * from t1;
+-------+-------+------+
| id1   | id2   |  id3 |
+-------+-------+------+
| 1.234 | 1.234 | 1    | 
+-------+-------+------+
1 row in set (0.00 sec)

```

这个时候，可以发现 id1、id2 字段中可以正常插入数据，而 id3 字段的小数位被截断。上面这个例子验证了上面提到的浮点数如果不写精度和标度，则会按照实际精度值显示，如果有精度和标度，则会自动将四舍五入后的结果插入，系统不会报错；定点数如果不写精度和标度，则按照默认值 decimal(10,0)来进行操作，并且如果数据超越了精度和标度值，系统
则会报错。

### BIT类型

对于 BIT（位）类型，用于存放位字段值，BIT(M)可以用来存放多位二进制数，M 范围从 1～64，如果不写则默认为 1 位。对于位字段，直接使用 SELECT 命令将不会看到结果，可以用bin()（显示为二进制格式）或者 hex()（显示为十六进制格式）函数进行读取。

下面的例子中，对测试表 t2 中的 bit 类型字段 id 做 insert 和 select 操作，这里重点观察一下select 的结果：

```sql
mysql> desc t2;
+-------+--------+------+-----+---------+-------+
| Field | Type | Null  | Key  | Default | Extra |
+-------+--------+------+-----+---------+-------+
| id    | bit(1) | YES |      | NULL    |       | 
+-------+--------+------+-----+---------+-------+
1 row in set (0.00 sec)
mysql> insert into t2 values(1);
Query OK, 1 row affected (0.00 sec)
mysql> select * from t2;
+------+
| id   |
+------+
|      | 
+------+
1 row in set (0.00 sec)

```

可以发现，直接 select * 结果为 NULL。改用 bin()和 hex()函数再试试：

```sql
mysql> select bin(id),hex(id) from t2;
+---------+---------+
| bin(id) | hex(id) |
+---------+---------+
| 1       | 1       | 
+---------+---------+
1 row in set (0.00 sec)

```

结果可以正常显示为二进制数字和十六进制数字。数据插入 bit 类型字段时，首先转换为二进制，如果位数允许，将成功插入；如果位数小于实际定义的位数，则插入失败，下面的例子中，在 t2 表插入数字 2，因为它的二进制码是“10”，而 id 的定义是 bit(1)，将无法进行插入：

```sql
mysql> insert into t2 values(2);
Query OK, 1 row affected, 1 warning (0.00 sec)
mysql> show warnings;
+---------+------+------------------------------------------------------+
| Level   | Code | Message                                              |
+---------+------+------------------------------------------------------+
| Warning | 1264 | Out of range value adjusted for column 'id' at row 1 | 
+---------+------+------------------------------------------------------+
1 row in set (0.01 sec)

```

将 ID 定义修改为 bit(2)后，重新插入，插入成功：

```sql
mysql> alter table t2 modify id bit(2);
Query OK, 1 row affected (0.02 sec)
Records: 1 Duplicates: 0 Warnings: 0
mysql> insert into t2 values(2);
Query OK, 1 row affected (0.00 sec)
mysql> select bin(id),hex(id) from t2;
+---------+---------+
| bin(id) | hex(id) |
+---------+---------+
| 1       | 1       | 
| 10      | 2       | 
+---------+---------+
2 rows in set (0.00 sec)
```

## 3.2 日期时间类型

MySQL 中有多种数据类型可以用于日期和时间的表示，不同的版本可能有所差异，表 3-2 中列出了 MySQL 5.0 中所支持的日期和时间类型。

![1584930839785](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200324155327-403493.png)

这些数据类型的主要区别如下：
 如果要用来表示年月日，通常用 DATE 来表示。
 如果要用来表示年月日时分秒，通常用 DATETIME 表示。
 如果只用来表示时分秒，通常用 TIME 来表示。
 如果需要经常插入或者更新日期为当前系统时间，则通常使用 TIMESTAMP 来表示。
TIMESTAMP 值返回后显示为“YYYY-MM-DD HH:MM:SS”格式的字符串，显示宽度固定为 19 个字符。如果想要获得数字值，应在 TIMESTAMP 列添加+0。
 如果只是表示年份，可以用 YEAR 来表示，它比 DATE 占用更少的空间。YEAR 有 2 位或4 位格式的年。默认是 4 位格式。在 4 位格式中，允许的值是 1901～2155 和 0000。在2 位格式中，允许的值是 70～69，表示从 1970～2069 年。MySQL 以 YYYY 格式显示 YEAR值。

每种日期时间类型都有一个有效值范围，如果超出这个范围，在默认的SQLMode下，系统会进行错误提示，并将以零值来进行存储。

### 数据插入方式

日期类型的插入格式有很多，包括整数（如2100）、字符串（如 2038-01-19 11:14:08）、函数（如 NOW()）等，大家可能会感到疑惑，到底什么样的格式才能够正确地插入到对应的日期字段中呢？下面以 DATETIME 为例进行介绍。

1. YYYY-MM-DD HH:MM:SS 或 YY-MM-DD HH:MM:SS 格式的**字符串**。允许“不严格”语法：任何标点符都可以用做日期部分或时间部分之间的间割符。例如，“98-12-31 11:30:45”、“98.12.31 11+30+45”、“98/12/31 11*30*45”和“98@12@31 11^30^45”是等价的。对于包括日期部分间割符的字符串值，如果日和月的值小于 10，不需要指定两位数。“1979-6-9”与“1979-06-09”是相同的。同样，对于包括时间部分间割符的字符串值，如果时、分和秒的值小于 10，不需要指定两位数。“1979-10-30 1:2:3”与“1979-10-30 01:02:03”相同。
2. YYYYMMDDHHMMSS 或 YYMMDDHHMMSS 格式的没有间割符的**字符串**，假定字符串对于日期类型是有意义的。例如，“19970523091528”和“970523091528”被解释为“1997-05-23 09:15:28”，但“971122129015”是不合法的（它有一个没有意义的分钟部分），将变为“0000-00-00 00:00:00”。
3. YYYYMMDDHHMMSS 或 YYMMDDHHMMSS 格式的**数字**，假定数字对于日期类型是有意义的。例如，19830905132800和830905132800被解释为“1983-09-05 13:28:00”。数字值应为 6、8、12 或者 14 位长。如果一个数值是 8 或 14 位长，则假定为YYYYMMDD 或 YYYYMMDDHHMMSS 格式，前 4 位数表示年。如果数字 是 6 或 12位长，则假定为 YYMMDD 或 YYMMDDHHMMSS 格式，前 2 位数表示年。其他数字被解释为仿佛用零填充到了最近的长度。
4. **函数**返回的结果，其值适合 DATETIME、DATE 或者 TIMESTAMP 上下文，例如 NOW()或 CURRENT_DATE。

## 3.3 字符串类型

MySQL 中提供了多种对字符数据的存储类型，不同的版本可能有所差异。以 5.0 版本为例，MySQL 包括了 CHAR、VARCHAR、BINARY、VARBINARY、BLOB、TEXT、ENUM 和 SET 等多种字符串类型。表 3-4 中详细列出了这些字符类型的比较。

![1584934259522](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200323113107-133040.png)

### 3.3.1 CHAR 和 VARCHAR 类型

CHAR 和 VARCHAR 很类似，都用来保存 MySQL 中较短的字符串(一个字符为一个字节)。二者的主要区别在于存储方式的不同：CHAR 列的长度**固定为创建表时声明的长度**，比如定义char(10)，那么不论你存储的数据是否达到了10个字节，都要占去10个字节的空间,不足的自动用空格填充。长度可以为从 0～255 的任何值；而 VARCHAR 列中的值为可变长字符串，长度可以指定为 0～255（5.0.3 以前）或者 65535（5.0.3以后）之间的值。在检索的时候，CHAR 列删除了尾部的空格，而 VARCHAR 则保留这些空格。`'ab  '`在CHAR中保存为`ab`,在VARCHAR中保存为`'ab  '`。

### 3.3.2 BINARY 和 VARBINARY 类型

BINARY 和 VARBINARY 类似于 CHAR 和 VARCHAR，不同的是它们包含二进制字符串而不包含非二进制字符串。在下面的例子中，对表 t 中的 binary 字段 c 插入一个字符，研究一下这个字符到底是怎么样存储的。
（1）创建测试表 t，字段为 c BINARY(3)：

```sql
mysql> CREATE TABLE t (c BINARY(3));
Query OK, 0 rows affected (0.14 sec)
```

（2）往 c 字段中插入字符“a”：

```sql
mysql> INSERT INTO t SET c='a';
Query OK, 1 row affected (0.06 sec)
```

（3）分别用以下几种模式来查看 c 列的内容：

```sql
mysql> select *,hex(c),c='a',c='a\0',c='a\0\0' from t10;
+------+--------+-------+---------+-----------+
| c    | hex(c) | c='a' | c='a\0' | c='a\0\0' |
+------+--------+-------+---------+-----------+
| a    | 610000 | 0     | 0       | 1         | 
+------+--------+-------+---------+-----------+
1 row in set (0.00 sec)
```

可以发现，当保存 BINARY 值时，在值的最后通过填充“0x00”（零字节）以达到指定的字段定义长度。从上例中看出，对于一个 BINARY(3)列，当插入时'a'变为'a\0\0'。

### 3.3.3 ENUM 类型

ENUM 中文名称叫枚举类型，它的值范围需要在创建表时通过枚举方式显式指定，对 1～255 个成员的枚举需要 1 个字节存储；对于 255～65535 个成员，需要 2 个字节存储。最多允许有 65535 个成员。下面往测试表 t 中插入几条记录来看看 ENUM 的使用方法。

（1）创建测试表 t，定义 gender 字段为枚举类型，成员为'M'和'F'：

```sql
mysql> create table t (gender enum('M','F'));
Query OK, 0 rows affected (0.14 sec)
```

（2）插入 4 条不同的记录：

```sql
mysql> INSERT INTO t VALUES('M'),('1'),('f'),(NULL);
Query OK, 4 rows affected (0.00 sec)
Records: 4 Duplicates: 0 Warnings: 0
mysql> select * from t;
+--------+
| gender |
+--------+
| M      |
| M      |
| F      |
| NULL   |
+--------+
4 rows in set (0.01 sec)

```

从上面的例子中，可以看出 ENUM 类型是忽略大小写的，对'M'、'f'在存储的时候将它们都转成了大写，还可以看出对于插入不在 ENUM 指定范围内的值时，并没有返回警告，而是插入了 enum('M','F')的第一值'M'，这点用户在使用时要特别注意。另外，ENUM 类型只允许从值集合中选取单个值，而不能一次取多个值。

### 3.3.4 SET 类型

Set 和 ENUM 类型非常类似，也是一个字符串对象，里面可以包含 0～64 个成员。根据
成员的不同，存储上也有所不同。
 1～8 成员的集合，占 1 个字节。
 9～16 成员的集合，占 2 个字节。
 17～24 成员的集合，占 3 个字节。
 25～32 成员的集合，占 4 个字节。
 33～64 成员的集合，占 8 个字节。
Set 和 ENUM 除了存储之外，最主要的区别在于 Set 类型一次可以选取多个成员，而 ENUM则只能选一个。下面的例子在表 t 中插入了多组不同的成员：

```sql
Create table t (col set （'a','b','c','d'）;
insert into t values('a,b'),('a,d,a'),('a,b'),('a,c'),('a');
mysql> select * from t;
+------+
| col  |
+------+
| a,b  |
| a,d  |
| a,b  |
| a,c  |
| a    |
+------+
5 rows in set (0.00 sec)

```

SET 类型可以从允许值集合中选择任意 1 个或多个元素进行组合，所以对于输入的值只要是在允许值的组合范围内，都可以正确地注入到 SET 类型的列中。对于超出允许值范围的值例如（'a,d,f'）将不允许注入到上面例子中设置的 SET 类型列中，而对于（'a,d,a'）这样包含重复成员的集合将只取一次，写入后的结果为“a,d”，这一点请注意。



## 3.4 数据类型中''长度''的作用

分析MySQL数据类型的长度

​       MySQL有几种数据类型可以限制类型的"长度"，有CHAR(Length)、VARCHAR(Length)、TINYINT(Length)、SMALLINT(Length)、MEDIUMINT(Length)、INT(Length)、BIGINT(Length)、FLOAT(Length,  Decimals)、DOUBLE(Length, Decimals)和DECIMAL(Length, Decimals)。

  然而，这些数据类型的长度，并不是都指数据的大小。具体说就是：
（1）对于CHAR、VARCAHR：4.0版本以下，varchar(20)，指的是20字节，如果存放UTF8汉字时，只能存6个（每个汉字3字节） ；5.0版本以上，varchar(20)，指的是20字符，无论存放的是数字、字母还是UTF8汉字（每个汉字3字节），都可以存放20个，最大大小是65532字节（[拓展：如何计算每个varchar储存时实际用了多少字节？(这篇文章的提到的计算公式其实还需要减去其他行包括非vachar类型占用的字节大小)](https://learn.blog.csdn.net/article/details/103341778#commentBox)）

（2）TINYINT、SMALLINT、MEDIUMINT、INT和BIGINT的长度，其实和数据的大小无关！Length指的是显示宽度，[可以参考这篇文章](https://www.cnblogs.com/EasonJim/p/7737029.html)

 （3）FLOAT(Length,  Decimals)、DOUBLE(Length, Decimals)和DECIMAL(Length, Decimals)，浮点数和定点数都可以用类型名称后加“(M,D)”的方式来进行表示，“(M,D)”表示该值一共显示 M 位数字（整数位+小数位），其中 D 位位于小数点后面，M 和 D 又称为精度和标度。例如，定义为 float(7,4)的一个列可以显示为-999.9999。MySQL 保存值时进行四舍五入，因此如果在 float(7,4)列内插入 999.00009，近似结果是 999.0001。