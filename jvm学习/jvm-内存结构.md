前言：文中出现的示例代码地址为：[gitee代码地址](https://gitee.com/gu_chun_bo/java-construct/tree/master/jvm%E5%AD%A6%E4%B9%A0/jvm)

本文结构：

![1583852076452](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200310225437-924504.png)



[TOC]

# 一：虚拟机的内存结构

什么是虚拟机内存结构？jvm在运行java程序中的时候会把它管理的内存划分为若干个不同的数据区域，这些区域就是内存结构啦！jdk内存结构如下所示：

![1583676593240](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200308220954-951122.png)

## 1.1 虚拟机栈

此区域是线程私有的，虚拟机栈描述的是Java<span style = "color:red">方法执行</span>的内存模型：每个方法被执行的时候都会同时创建一个栈帧(stack frame)用于存储局部变量表、操作数栈、动态链接、方法出口等信息(如果想对栈帧有进一步的理解可以看看我写的Class文件结构笔记)。局部变量表中存放了编译器可知的数据类型1.存放基本数据类型 2.引用数据类型。如果是基本数据类型，那么将数据的值直接放到栈帧里面，如果是引用类型指向的对象是放在堆上的。【reference类型，它不同于对象本身，可能是一个指向对象起始地址的引用指针，也可能是指向代表对象的句柄或者其它与此对象相关的位置】

那我来举一个方法中对象创建的实例：

1. 代码

   ```java
   public void method1(){
       Object obj = new Object();
   }
   ```

2. 实例的内存分配是这样滴，生成了2部分的内存区域 1)obj这个引用变量(reference),因为是方法内的变量,放到 JVM Stack里面2)真正 Object的实例对象,放到Heap里面

   上述的new语句一共消耗12个 bytes,JM规定引用占4个 bytes(在 JVM Stack),而空对象是8个 bytes(在Heap)方法结束后,对应 Stack中的变量马上回收,但是Heap中的对象要等到GC来回收




### 异常

Java 虚拟机栈会出现两种错误：StackOverFlowError 和 OutOfMemoryError。

- **StackOverFlowError：** 若 Java 虚拟机栈的内存大小不允许动态扩展，那么当线程请求栈的深度超过当前 Java 虚拟机栈的最大深度的时候，就抛出 StackOverFlowError 错误。
- **OutOfMemoryError：** 若 Java 虚拟机栈的内存大小允许动态扩展，且当线程请求栈时内存用完了，无法再动态扩展了，此时抛出 OutOfMemoryError 错误。



## 1.2 本地方法栈

此区域是线程私有的，与虚拟机栈所发挥的作用是非常相似的，不过主要用于处理native方法。



## 1.3 程序计数器

此区域是线程私有的，我们知道线程执行的是一条条指令，那么线程是如何知道执行到哪条指令了呢？就是通过程序计数器来记录的，简单点说就是，程序计数器是当前线程所执行的字节码的行号指示器。



## 1.4 堆

此区域是线程共享的，堆（heap） JVM的最大的内存空间，此区域的唯一目的就是保存对象实例。堆空间可以分为新生代和老年代，其中的新生代可以继续细分为Eden空间 From Survivor 空间和 To Survivor空间  Old Generation

### 异常

**OutOfMemoryError: GC Overhead Limit Exceeded** ： 当JVM花太多时间执行垃圾回收并且只能回收很少的堆空间时，就会发生此错误。

**java.lang.OutOfMemoryError: Java heap space** :假如在创建新的对象时, 堆内存中的空间不足以存放新创建的对象, 就会引发`java.lang.OutOfMemoryError: Java heap space` 错误。(和本机物理内存无关，和你配置的内存大小有关！)



虚拟机参数

- -XX:+HeapDumpOnOutOfMemoryError 将错误信息文件输出
- -Xms5m -Xmx5m  初始和最大的堆内存，通常设置成一样的，防止垃圾回收之后有堆抖动的问题
- Xmn10m 新生代的容量
- -XX: SurvivorRatio=8   eden 和survivor的所占空间大小比例为 8：1

## 1.5 方法区

> 方法区与永久代
>
> 这两个是非常容易混淆的概念，永久代的对象放在方法区中，就会想当然地认为，方法区就等同于持久代的内存区域。事实上两者是这样的关系：《Java虚拟机规范》只是规定了有方法区这么个概念和它的作用，并没有规定如何去实现它。那么，在不同的 JVM 上方法区的实现肯定是不同的了。 同时大多数用的JVM都是Sun公司的HotSpot，使用永久代来实现方法区。换句话说：方法区是一种规范，永久代是Hotspot针对这一规范的一种实现。
>

### 元空间

此区域是线程共享的，Method Area 存储元信息。以前是为永久代（Permanent Generation），但是从jdk1.8 开始，已经彻底废弃了永久代。使用元空间(Meta space)，元空间使用的是操作系统的本地内存，有专门的元空间虚拟机进行内存管理，如果不指定大小的话，随着更多类的创建，虚拟机会耗尽所有可用的系统内存。元空间存放的不是对象的信息，而是存放如一个类的Class的结构信息【Class结构信息包括常量池，字段描述，方法描述等】，常量，静态变量，即时编译编译后的代码。在元空间中，类和其元数据的生命周期和其对应的类加载器是相同的。换句话说，只要类加载器存活，其加载的类的元数据也是存活的，因而不会被回收掉。关于元空间参考：https://www.infoq.cn/article/java-permgen-Removed

java的虚拟机规范没有规定要在方法区进行垃圾回收，这块区域的垃圾回收性价比比较低。不过当前的商业jvm中都实现了对方法区进行垃圾回收，主要是回收两部分内容：废弃常量和无用类。其中的类回收需要满足三个条件：1.该类的所有的实例都被GC，也就是jvm中不存在该类的任何实例；2. 加载该类的classloader已经被GC 3. 该类对应的java.lang.Class没有在任何地方被引用，如不能在任何地方通过反射访问该类的方法。   在大量使用反射，动态代理，cglib等字节码框架或动态生成jsp或osgi这类频繁自定义classloader的场景都需要jvm具备类卸载的支持以保证方法区不会溢出。

### 异常

元空间溢出时会得到如下错误： `java.lang.OutOfMemoryError: MetaSpace`

虚拟机参数

- -XX:MetaspaceSize=N                设置 Metaspace 的初始（和最小大小）
- -XX:MaxMetaspaceSize=N         设置 Metaspace 的最大大小

## 1.6 运行时常量池

此区域是线程共享的，运行时常量池是方法区的一部分，是一块内存区域。

![1583680569679](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200308231610-754419.png)

### 异常

既然运行时常量池是方法区的一部分，自然受到方法区内存的限制，当常量池无法再申请到内存时会抛出 OutOfMemoryError 错误。

### 运行时常量池和静态常量池的区别

这里我直接引用一篇文章，讲得真的太棒了！**https://blog.csdn.net/wangbiao007/article/details/78545189**

## 1.7 直接内存

此区域是线程共享的，直接内存（Direct Memory）并不是java虚拟机直接管理的内存区域，它是与java nio密切相关，jvm是通过DirectByteBuffer来操作直接内存。



## 1.8 虚拟机对象揭秘

### 1.8.1  java中创建对象的五大流程

![1583678024491](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200310200728-85842.png)

1. 类加载检查，虚拟机首先会检查这个指令的参数能否在常量池中定位到这个类的符号引用，并且检查符号引用代表的类是否已经被加载，连接和初始化过，如果没有，那么就得先执行相应的类加载过程
2. 内配内存：在堆内存中创建出对象的实例
   1. 在堆内存中分配内存中的时候分为两种方式
      1. 指针碰撞（前提是堆中的空间通过一个指针进行分割，一侧是已经被占用的空间，另一侧是未被占用的空间）
      2. 空闲列表（前提是堆内存空间已经被使用和未被使用的空间是交织在一起的，这时虚拟机就要通过一个列表记录哪些空间是可以用的，哪些是已经被使用的。接下来找出可以容纳下新创建对象的且未被使用的空间，在空间存放该对象，同时还要修改表上的记录） 
   2. 内存分配的方式，取决于堆是否规整，java堆是否规整，取决于GC收集器的算法是标记清除(不规整)还是标记整理或复制算法(规整)。
3. 为初始化完对象的实例变量赋予默认的初始值，这一步骤保证了对象可以在不赋初值的情况下正常使用
4. 设置对象头，对象头是什么，等等再讲！
5. 为实例变量赋予正确的值，即我们程序员为它分配的值，实例变量的赋值是在jvm自动生成的<init>方法中进行的，很诧异吧，实例变量的值是在方法里进行赋值的！



### 1.8.2 对象的内存布局

如下图所示，对象再内存中的布局可以分为三块区域：对象头，**实例数据**，和对齐补充

![1583678624634](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200308224345-655905.png)

#### 对象头

对象头包括两部分的信息，第一部分部分为Mark Word，第二部分为是指向Class对象的指针，虚拟机通过这个指针确定这个对象是哪个类的实例

![1583651065372](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200308223951-617147.png)

Mark Word 用于储存自身运行时的数据（哈希码）

![1583651590160](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200308151311-525787.png)

#### 实例数据

**实例数据**是对象用于真正储存的有效信息，也就是在程序中所定义的各种类型的字段内容

#### 对齐补充

对齐填充部分不是必然存在的，也没有什么特别的含义，仅仅起占位作用。 因为 Hotspot 虚拟机的自动内存管理系统要求对象起始地址必须是 8 字节的整数倍，换句话说就是对象的大小必须是 8 字节的整数倍。



### 1.8.3 对象的定位

我们通过reference来操作堆上的具体的对象，对象的访问方式有两种，一种是使用句柄，一种是使用直接指针

1. 使用句柄，在java堆中划出一部分作为句柄池，reference中储存的就是对象的句柄地址，而句柄中包含了对象**实例数据**和Class数据的具体地址信息

   ![对象的访问定位-使用句柄](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200308230059-932678.png)

2. 直接指针，  如果使用直接指针访问，那么 Java 堆对象的布局中就必须考虑如何放置访问Class数据的相关信息，而 reference 中存储的直接就是对象的地址。

   ![对象的访问定位-直接指针](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200310200814-832353.png)

使用句柄的好处就是reference中储存的是稳定的句柄地址，如果对象移动，那么并不需要修改reference，只需要修改句柄中**实例数据**的指针，而如果使用直接指针访问方式最大的好处就是速度快，它节省了一次指针定位的时间开销。



## 1.9 字符串常量池

[一天一夜搞懂String Pool(字符串常量池)](https://gitee.com/gu_chun_bo/java-construct/blob/master/jvmlearing/SubFolder/jvm-%E4%B8%80%E5%A4%A9%E4%B8%80%E5%A4%9C%E6%90%9E%E6%87%82String%20Pool(%E5%AD%97%E7%AC%A6%E4%B8%B2%E5%B8%B8%E9%87%8F%E6%B1%A0).md)

## 1.10 参考链接

- [JavaGuide大佬](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java%E5%86%85%E5%AD%98%E5%8C%BA%E5%9F%9F?id=java-%e5%86%85%e5%ad%98%e5%8c%ba%e5%9f%9f%e8%af%a6%e8%a7%a3)

