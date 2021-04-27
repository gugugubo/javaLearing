# **[Java 内存区域](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域)**

## [二 运行时数据区域](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=二-运行时数据区域)

### [2.1 程序计数器](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=_21-程序计数器)

1. 字节码解释器通过改变程序计数器来依次读取指令，从而实现代码的流程控制，如：顺序执行、选择、循环、异常处理。
2. 在多线程的情况下，程序计数器用于记录当前线程执行的位置，从而当线程被切换回来的时候能够知道该线程上次运行到哪儿了。



### [2.2 Java 虚拟机栈](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=_22-java-虚拟机栈)

**与程序计数器一样，Java 虚拟机栈也是线程私有的，它的生命周期和线程相同，描述的是 Java 方法执行的内存模型，每次方法调用的数据都是通过栈传递的。**

**Java 内存可以粗糙的区分为堆内存（Heap）和栈内存 (Stack),其中栈就是现在说的虚拟机栈，或者说是虚拟机栈中局部变量表部分。** （实际上，Java 虚拟机栈是由一个个栈帧组成，而每个栈帧中都拥有：局部变量表、操作数栈、动态链接、方法出口信息。）

**局部变量表主要存放了编译期可知的各种数据类型**（boolean、byte、char、short、int、float、long、double）、**对象引用**（reference 类型，它不同于对象本身，可能是一个指向对象起始地址的引用指针，也可能是指向一个代表对象的句柄或其他与此对象相关的位置）。





### [2.5 方法区](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=_25-方法区)

方法区与 Java 堆一样，是各个线程共享的内存区域，它用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。虽然 **Java 虚拟机规范把方法区描述为堆的一个逻辑部分**，但是它却有一个别名叫做 **Non-Heap（非堆）**，目的应该是与 Java 堆区分开来。





### [2.6 运行时常量池](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=_26-运行时常量池)

运行时常量池是方法区的一部分。Class 文件中除了有类的版本、字段、方法、接口等描述信息外，还有常量池表（用于存放编译期生成的各种字面量和符号引用）

`既然运行时常量池是方法区的一部分，自然受到方法区内存的限制，当常量池无法再申请到内存时会抛出 OutOfMemoryError 错误。

~~**JDK1.7 及之后版本的 JVM 已经将运行时常量池从方法区中移了出来，在 Java 堆（Heap）中开辟了一块区域存放运行时常量池。**~~ 

> 修正([issue747](https://github.com/Snailclimb/JavaGuide/issues/747)，[reference](https://blog.csdn.net/q5706503/article/details/84640762))： 
>
> 1. **JDK1.7之前运行时常量池逻辑包含字符串常量池存放在方法区, 此时hotspot虚拟机对方法区的实现为永久代**
> 2. **JDK1.7 字符串常量池被从方法区拿到了堆中, 这里没有提到运行时常量池,也就是说字符串常量池被单独拿到堆,运行时常量池剩下的东西还在方法区, 也就是hotspot中的永久代** 。
> 3. **JDK1.8 hotspot移除了永久代用元空间(Metaspace)取而代之, 这时候字符串常量池还在堆, 运行时常量池还在方法区, 只不过方法区的实现从永久代变成了元空间(Metaspace)** 





## [三 HotSpot 虚拟机对象探秘](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=三-hotspot-虚拟机对象探秘)

### [3.1 对象的创建](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=_31-对象的创建)

下图便是 Java 对象的创建过程，我建议最好是能默写出来，并且要掌握每一步在做什么。 ![Java创建对象的过程](https://snailclimb.gitee.io/javaguide/docs/java/jvm/pictures/java内存区域/Java创建对象的过程.png)

#### [Step1:类加载检查](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=step1类加载检查)

 虚拟机遇到一条 new 指令时，首先将去检查这个指令的参数是否能在常量池中定位到这个类的符号引用，并且检查这个符号引用代表的类是否已被加载过、解析和初始化过。如果没有，那必须先执行相应的类加载过程。

#### [Step2:分配内存](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=step2分配内存)

在**类加载检查**通过后，接下来虚拟机将为新生对象**分配内存**。对象所需的内存大小在类加载完成后便可确定，为对象分配空间的任务等同于把一块确定大小的内存从 Java 堆中划分出来。**分配方式**有 **“指针碰撞”** 和 **“空闲列表”** 两种，**选择哪种分配方式由 Java 堆是否规整决定，而 Java 堆是否规整又由所采用的垃圾收集器是否带有压缩整理功能决定**。

#### [Step3:初始化零值](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=step3初始化零值)

内存分配完成后，虚拟机需要将分配到的内存空间都初始化为零值（不包括对象头），这一步操作保证了对象的实例字段在 Java 代码中可以不赋初始值就直接使用，程序能访问到这些字段的数据类型所对应的零值。

#### [Step4:设置对象头](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=step4设置对象头)

初始化零值完成之后，**虚拟机要对对象进行必要的设置**，例如这个对象是哪个类的实例、如何才能找到类的元数据信息、对象的哈希码、对象的 GC 分代年龄等信息。 **这些信息存放在对象头中。** 另外，根据虚拟机当前运行状态的不同，如是否启用偏向锁等，对象头会有不同的设置方式。

#### [Step5:执行 init 方法](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=step5执行-init-方法)

 在上面工作都完成之后，从虚拟机的视角来看，一个新的对象已经产生了，但从 Java 程序的视角来看，对象创建才刚开始，`<init>` 方法还没有执行，所有的字段都还为零。所以一般来说，执行 new 指令之后会接着执行 `<init>` 方法，把对象按照程序员的意愿进行初始化，这样一个真正可用的对象才算完全产生出来。

### [3.3 对象的访问定位](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/Java内存区域?id=_33-对象的访问定位)

建立对象就是为了使用对象，我们的 Java 程序通过栈上的 reference 数据来操作堆上的具体对象。对象的访问方式由虚拟机实现而定，目前主流的访问方式有**①使用句柄**和**②直接指针**两种：

1. **句柄：** 如果使用句柄的话，那么 Java 堆中将会划分出一块内存来作为句柄池，reference 中存储的就是对象的句柄地址，而句柄中包含了对象实例数据与类型数据各自的具体地址信息；  ![对象的访问定位-使用句柄](https://snailclimb.gitee.io/javaguide/docs/java/jvm/pictures/java内存区域/对象的访问定位-使用句柄.png)
2. **直接指针：**  如果使用直接指针访问，那么 Java 堆对象的布局中就必须考虑如何放置访问类型数据的相关信息，而 reference 中存储的直接就是对象的地址。

![对象的访问定位-直接指针](https://snailclimb.gitee.io/javaguide/docs/java/jvm/pictures/java内存区域/对象的访问定位-直接指针.png)

**这两种对象访问方式各有优势。使用句柄来访问的最大好处是 reference 中存储的是稳定的句柄地址，在对象被移动时只会改变句柄中的实例数据指针，而 reference  本身不需要修改。使用直接指针访问方式最大的好处就是速度快，它节省了一次指针定位的时间开销。**





# [类的生命周期](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/类加载过程?id=类的生命周期)

一个类的完整生命周期如下：

![img](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/2019-11/类加载过程-完善.png)

## [类加载过程](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/类加载过程?id=类加载过程)

Class 文件需要加载到虚拟机中之后才能运行和使用，那么虚拟机是如何加载这些 Class 文件呢？

系统加载 Class 类型的文件主要三步:**加载->连接->初始化**。连接过程又可分为三步:**验证->准备->解析**。

![img](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/2019-6/类加载过程.png)

### [加载](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/类加载过程?id=加载)

类加载过程的第一步，主要完成下面3件事情：

1. 通过全类名获取定义此类的二进制字节流
2. 将字节流所代表的静态存储结构转换为方法区的运行时数据结构
3. 在内存中生成一个代表该类的 Class 对象,作为方法区这些数据的访问入口



### [验证](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/类加载过程?id=验证)





### [准备](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/类加载过程?id=准备)

**准备阶段是正式为类变量分配内存并设置类变量初始值的阶段**，这些内存都将在方法区中分配。





### [解析](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/类加载过程?id=解析)

解析阶段是虚拟机将常量池内的符号引用替换为直接引用的过程。解析动作主要针对类或接口、字段、类方法、接口方法、方法类型、方法句柄和调用限定符7类符号引用进行。



### [初始化](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/类加载过程?id=初始化)

初始化是类加载的最后一步，也是真正执行类中定义的 Java 程序代码(字节码)，初始化阶段是执行初始化方法 `<clinit> ()`方法的过程。

对于`<clinit>（）` 方法的调用，虚拟机会自己确保其在多线程环境中的安全性。因为 `<clinit>（）` 方法是带锁线程安全，所以在多线程环境下进行类初始化的话可能会引起死锁，并且这种死锁很难被发现。

对于初始化阶段，虚拟机严格规范了有且只有5种情况下，必须对类进行初始化(只有主动去使用类才会初始化类)：

1. 当遇到 new 、 getstatic、putstatic或invokestatic 这4条直接码指令时，比如 new 一个类，读取一个静态字段(未被 final 修饰)、或调用一个类的静态方法时。
   - 当jvm执行new指令时会初始化类。即当程序创建一个类的实例对象。
   - 当jvm执行getstatic指令时会初始化类。即程序访问类的静态变量(不是静态常量，常量会被加载到运行时常量池)。
   - 当jvm执行putstatic指令时会初始化类。即程序给类的静态变量赋值。
   - 当jvm执行invokestatic指令时会初始化类。即程序调用类的静态方法。
2. 使用 `java.lang.reflect` 包的方法对类进行反射调用时如Class.forname("..."),newInstance()等等。 ，如果类没初始化，需要触发其初始化。
3. 初始化一个类，如果其父类还未初始化，则先触发该父类的初始化。
4. 当虚拟机启动时，用户需要定义一个要执行的主类 (包含 main 方法的那个类)，虚拟机会先初始化这个类。
5. MethodHandle和VarHandle可以看作是轻量级的反射调用机制，而要想使用这2个调用， 就必须先使用findStaticVarHandle来初始化要调用的类。
6. **「补充，来自[issue745](https://github.com/Snailclimb/JavaGuide/issues/745)」** 当一个接口中定义了JDK8新加入的默认方法（被default关键字修饰的接口方法）时，如果有这个接口的实现类发生了初始化，那该接口要在其之前被初始化。









## [类加载器总结](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/类加载器?id=类加载器总结)

JVM 中内置了三个重要的 ClassLoader，除了 BootstrapClassLoader 其他类加载器均由 Java 实现且全部继承自`java.lang.ClassLoader`：

1. **BootstrapClassLoader(启动类加载器)** ：最顶层的加载类，由C++实现，负责加载 `%JAVA_HOME%/lib`目录下的jar包和类或者或被 `-Xbootclasspath`参数指定的路径中的所有类。
2. **ExtensionClassLoader(扩展类加载器)** ：主要负责加载目录 `%JRE_HOME%/lib/ext` 目录下的jar包和类，或被 `java.ext.dirs` 系统变量所指定的路径下的jar包。
3. **AppClassLoader(应用程序类加载器)** :面向我们用户的加载器，负责加载当前应用classpath下的所有jar包和类。





## [双亲委派模型](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/类加载器?id=双亲委派模型)

### [双亲委派模型的好处](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/类加载器?id=双亲委派模型的好处)

双亲委派模型保证了Java程序的稳定运行，可以避免类的重复加载（JVM 区分不同类的方式不仅仅根据类名，相同的类文件被不同的类加载器加载产生的是两个不同的类），也保证了 Java 的核心 API  不被篡改。如果没有使用双亲委派模型，而是每个类加载器加载自己的话就会出现一些问题，比如我们编写一个称为 `java.lang.Object` 类的话，那么程序运行的时候，系统就会出现多个不同的 `Object` 类。





# [JVM 垃圾回收](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/JVM垃圾回收?id=jvm-垃圾回收)

## [3 垃圾收集算法](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/JVM垃圾回收?id=_3-垃圾收集算法)



### [3.1 标记-清除算法](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/JVM垃圾回收?id=_31-标记-清除算法)

### [3.2 复制算法](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/JVM垃圾回收?id=_32-复制算法)

### [3.3 标记-整理算法](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/JVM垃圾回收?id=_33-标记-整理算法)





### [3.4 分代收集算法](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/JVM垃圾回收?id=_34-分代收集算法)

当前虚拟机的垃圾收集都采用分代收集算法，这种算法没有什么新的思想，只是根据对象存活周期的不同将内存分为几块。一般将 java 堆分为新生代和老年代，这样我们就可以根据各个年代的特点选择合适的垃圾收集算法。

**比如在新生代中，每次收集都会有大量对象死去，所以可以选择复制算法，只需要付出少量对象的复制成本就可以完成每次垃圾收集。而老年代的对象存活几率是比较高的，而且没有额外的空间对它进行分配担保，所以我们必须选择“标记-清除”或“标记-整理”算法进行垃圾收集。**

**延伸面试问题：** HotSpot 为什么要分为新生代和老年代？

根据上面的对分代收集算法的介绍回答。



## [4 垃圾收集器](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/JVM垃圾回收?id=_4-垃圾收集器)





# 面试问题

1. jvm运行时内存区域？

   1. 程序计数器

      1. 字节码解释器通过改变程序计数器来依次读取指令，从而实现代码的流程控制，如：顺序执行、选择、循环、异常处理。
      2. 在多线程的情况下，程序计数器用于记录当前线程执行的位置，从而当线程被切换回来的时候能够知道该线程上次运行到哪儿了。
      3. 程序计数器是唯一一个不会出现 `OutOfMemoryError` 的内存区域

   2. 虚拟机栈：

      1. Java 虚拟机栈是由一个个栈帧组成，而每个栈帧中都拥有：局部变量表、操作数栈、动态链接、方法出口信息；局部变量表主要存放了编译期可知的各种数据类型（boolean、byte、char、short、int、float、long、double）、对象引用
      2. Java 虚拟机栈会出现两种错误：`StackOverFlowError` 和 `OutOfMemoryError`。
         - `StackOverFlowError`： 若 Java 虚拟机栈的内存大小不允许动态扩展，那么当线程请求栈的深度超过当前 Java 虚拟机栈的最大深度的时候，就抛出 StackOverFlowError 错误。
         - `OutOfMemoryError`： 若 Java 虚拟机堆中没有空闲内存，并且垃圾回收器也无法提供更多内存的话。就会抛出 OutOfMemoryError 错误。

   3. 本地方法栈： 在 HotSpot 虚拟机中和 Java 虚拟机栈合二为一

   4. 堆：

      1. `OutOfMemoryError: GC Overhead Limit Exceeded` ： 当JVM花太多时间执行垃圾回收并且只能回收很少的堆空间时，就会发生此错误。

         `java.lang.OutOfMemoryError: Java heap space` :假如在创建新的对象时, 堆内存中的空间不足以存放新创建的对象, 就会引发`java.lang.OutOfMemoryError: Java heap space` 错误。(和本机物理内存无关，和你配置的内存大小有关！)

   5. 方法区，现在的实现是元空间，元空间使用的是直接内存：

      1. 用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据
      2. 当你元空间溢出时会得到如下错误： `java.lang.OutOfMemoryError: MetaSpace`

   6. 运行时常量池：是方法区的一部分

      1. 运行时常量池：Class 文件中除了有类的版本、字段、方法、接口等描述信息外，还有常量池表（用于存放编译期生成的各种字面量和符号引用）
      2. 既然运行时常量池是方法区的一部分，自然受到方法区内存的限制，当常量池无法再申请到内存时会抛出 OutOfMemoryError 错误。

   7. 直接内存 (非运行时数据区的一部分)

2. 静态常量池有啥？静态常量池包含的字面量和符号引用：一字面量：字符串，基本数据类型，final修饰的常量；----------  二符号引用：1.类的全限定名，2.字段名和属性，3.方法名和属性 && && &&注意：符号引用其实引用的就是常量池里面的字符串，但符号引用不是直接存储字符串，而是存储字符串在常量池里的索引。

3. 方法区中有啥？[方法区和常量池的关系](https://blog.csdn.net/wangbiao007/article/details/78545189)：方法区里存储着class文件的信息和动态常量池--------class文件的信息包括类信息和静态常量池。可以将类的信息是对class文件内容的一个框架，里面具体的内容通过静态常量池来存储。---------动态常量池里的内容除了是静态常量池里的内容外，还将静态常量池里的符号引用转变为直接引用

4. 

5. 

6. 垃圾回收算法？https://blog.csdn.net/wuzhiwei549/article/details/80563134

   1. 标记清除：首先标记出所有不需要回收的对象； 1.效率问题：标记和清除过程的效率都不高  2. 空间问题：标记清除之后会产生大量不连续的内存碎片
   2. 标记复制：1.效率问题：在对象存活率较高时，复制操作次数多，效率降低  2. 空间问题：內存缩小了一半；需要額外空间做分配担保
   3. 标记整理：

7. 垃圾回收器？

   1. Serial收集器：简单高效

   2. Serial Old：Serial 的老年版本

   3. ParNew 收集器：其实就是 Serial 收集器的多线程版本，除了使用多线程进行垃圾收集外，其余行为（控制参数、收集算法、回收策略等等）和 Serial 收集器完全一样

   4. Parallel Scavenge ：吞吐量高

   5. Parallel Old ：收集器的老年代版本

   6. CMS（Concurrent Mark Sweep）收集器是一种以获取最短回收停顿时间为目标的收集器
      1. MS以牺牲CPU资源的代价来减少用户线程的停顿。当CPU个数少于4的时候,有可能对吞吐量影响非常大；
      2. CMS收集器无法处理浮动垃圾（Floating Garbage）,即第一次标记，认为某个对象不是垃圾，但是在CMS线程和用户线程在并发执行的过程中此对象可能变成了垃圾，那么CMS无法在这次的垃圾回收中将它回收掉。
      3. 由于基于MS算法即Mark-Sweep，收集结束时会带来碎片问题，空间碎片过多会给大对象分配带来很大麻烦，望往往出现老年代还有很大的空间剩余，但是无法找到足够大的连续空间来分配当前对象，不得不提前进行一次Full GC。无法处理这些垃圾可能出现”concurrent mode failure“失败而导致另一次Full GC的产生。
      
   7. 拓展：并行和并发

      1. ![图片](https://mmbiz.qpic.cn/mmbiz_jpg/Z6bicxIx5naJr6V2cHcuYObZ7sh4aHQKLsXsibtn5q5PLEvACtCkB2DVTEZ5MyRC4KnKL8uVBEoZMYFAHyle3WJA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
      2. ![图片](https://mmbiz.qpic.cn/mmbiz_jpg/Z6bicxIx5naJr6V2cHcuYObZ7sh4aHQKLd3GJNGMLYtEAXgjlU8OeNThEZyacDlkXP3mleQjhfeWic2wsWT7zElA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
      3. ![图片](https://mmbiz.qpic.cn/mmbiz_png/Z6bicxIx5naJr6V2cHcuYObZ7sh4aHQKLiaNovQyk5kTDpKyZY4bSJicn9HMl0OPvUWPwqmI89uicBY1LRbNNNoicyw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

      

8. JVM内存分配与回收?

   1. 内存分配：
      1. 空间分配担保（先进行minorGC，把`Survivor`无法容纳的对象直接进入老年代）；
      2. 大对象直接进入老年代，大对象就是需要大量连续内存空间的对象（比如：字符串、数组）；      
      3. 长期存活的对象将进入老年代，通过参数 `-XX:MaxTenuringThreshold` 来设置
   2. 垃圾回收
      1. 垃圾回收的算法：引用计数算法    根搜索算法
      2. 哪些对象可以作为 GC ROOTS？1.虚拟机栈(栈帧中的本地变量表)中引用的对象2.本地方法栈(Native 方法)中引用的对象3.方法区中类静态属性引用的对象4.方法区中常量引用的对象
      3. GC的时机：
         1. Scavenge GC (Minor GC）：对新生代，触发时机是在新对象生成时，Eden空间满了，理论上Eden区大多数对象会在 Scavenge GC回收，复制算法的执行效率会很高， Scavenge GC时间比较短  ；     
         2. Full GC：对整个JVM进行整理，包括 Young、Old和Perm（永久代，jdk8没有，jdk为元空间），主要的触发时机:1)Old满了【1.大对象把它塞满了2.主要是在触发minorGC的时候判断老年代够不够，不够就进行FullGC，够的话仅是MinorGC】2)Perm满了3) system.gc()   Full GC的执行效率很低，尽量减少 Full GC
      4. GC时机-线程角度：
         1. 主动式中断:当GC需要中断线程的时候，不直接对线程操作，仅仅简单地设置一个标志，各个线程执行时主动去轮询这个标志，发现中断标志为真时就自己中断挂起；      
         2. 抢占式中断:它不需要线程的执行代码主动去配合,在GC发生时,首先把所有线程全部中断,如果有线程中断的地方不在安全点上,就恢复线程,让它“跑”到安全点上

9. 

10. 

11. 怎么解决垃圾回收中的跨代引用问题？https://mp.weixin.qq.com/s/t1Cx1n6irN1RWG8HQyHU2w

    1. 第一种：针对老年代回收
       1. 生代对象持有老年代中对象的引用，这种情况称为“跨代引用”
       2. 增加可中断的并发预请理阶段，并增加参数强制Minor   (由于跨代引用的存在，CMS在Remark阶段必须扫描整个堆，同时为了避免扫描时新生代有很多对象，增加了可中断的预清理阶段用来等待Minor  GC的发生。只是该阶段有时间限制，如果超时等不到Minor  GC，Remark时新生代仍然有很多对象，我们的调优策略是，通过参数强制Remark前进行一次Minor GC，从而降低Remark阶段的时间。)
    2. 第二种：针对新生代回收
       1. 即老年代可能持有新生代对象引用，所以Minor GC时也必须扫描老年代
       2. 卡表的具体策略是将老年代的空间分成大小为512B的若干张卡（card）。卡表本身是单字节数组，数组中的每个元素对应着一张卡，当发生老年代引用新生代时，虚拟机将该卡对应的卡表元素设置为适当的值。之后Minor GC时通过扫描卡表就可以很快的识别哪些卡中存在老年代指向新生代的引用

12. 怎么排查 OOM？https://mp.weixin.qq.com/s/MAQEmIpnMMN6GGD9WbPUFw

    1. 确认是不是内存本身就分配过小    jmap -heap 10765
    2. 找到最耗内存的对象     jmap -histo:live 10765 | more
       1. 如果发现某类对象占用内存很大（例如几个G），很可能是类对象创建太多，且一直未释放。例如：
          - 申请完资源后，未调用close()或dispose()释放资源
          - 消费者消费速度慢（或停止消费了），而生产者不断往队列中投递任务，导致队列中任务累积过多
    3. 确认是否是资源耗尽
       1. 工具：
          - pstree(查看进程树)
          - netstat（查看网络连接）

13. 哪些情况会出现 OOM？  https://mp.weixin.qq.com/s/MAQEmIpnMMN6GGD9WbPUFw

    1. 有可能是内存分配确实过小，而正常业务使用了大量内存
    2. 某一个对象被频繁申请，却没有释放，内存不断泄漏，导致内存耗尽
    3. 某一个资源被频繁申请，系统资源耗尽，例如：不断创建线程，不断发起网络连接

14. 

15. 

16. 四种引用？

17. 虚引用与软引用和弱引用的一个区别?（虚引用的作用！）：虚引用主要用来跟踪对象被垃圾回收的活动。---------------- 引用必须和引用队列（ReferenceQueue）联合使用，当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，把这个虚引用加入到与之关联的引用队列中。程序可以通过判断引用队列中是否已经加入了虚引用

18. 软引用的作用：软引用可用来实现内存敏感的高速缓存。---------------- 软引用可以加速 JVM 对垃圾内存的回收速度，可以维护系统的运行安全，防止内存溢出（OutOfMemory）等问题的产生

19. 

20. 

21. 类加载机制  加载---连接--初始化（初始化中主动使用的五个点）--卸载（卸载类需要满足3个要求）

    1. 初始化
       1. 当遇到 new 、 getstatic、putstatic或invokestatic 这4条直接码指令时，比如 new 一个类，读取一个静态字段(未被 final 修饰)、或调用一个类的静态方法时。
          - 当jvm执行new指令时会初始化类。即当程序创建一个类的实例对象。
          - 当jvm执行getstatic指令时会初始化类。即程序访问类的静态变量(不是静态常量，常量会被加载到运行时常量池)。
          - 当jvm执行putstatic指令时会初始化类。即程序给类的静态变量赋值。
          - 当jvm执行invokestatic指令时会初始化类。即程序调用类的静态方法。
       2. 使用 `java.lang.reflect` 包的方法对类进行反射调用时如Class.forname("..."),newInstance()等等。 ，如果类没初始化，需要触发其初始化。
       3. 初始化一个类，如果其父类还未初始化，则先触发该父类的初始化。
       4. 当虚拟机启动时，用户需要定义一个要执行的主类 (包含 main 方法的那个类)，虚拟机会先初始化这个类。
       5. MethodHandle和VarHandle可以看作是轻量级的反射调用机制，而要想使用这2个调用， 就必须先使用findStaticVarHandle来初始化要调用的类。
       6. **「补充，来自[issue745](https://github.com/Snailclimb/JavaGuide/issues/745)」** 当一个接口中定义了JDK8新加入的默认方法（被default关键字修饰的接口方法）时，如果有这个接口的实现类发生了初始化，那该接口要在其之前被初始化。
    2. 卸载？
       1. 该类的所有的实例对象都已被GC，也就是说堆不存在该类的实例对象。
       2. 该类没有在其他任何地方被引用
       3. 该类的类加载器的实例已被GC

22. 双亲委派模式有什么好处？1.可以确保java核心库的类型安全 ----2.可以确保java核心类型所提供的类不会被自定义的类所覆盖掉(因为会先向上查找)

23. 怎么打破双亲委托机制？为了避免双亲委托机制，我们可以自己定义一个类加载器，然后重写 loadClass() 即可。
    完善修正（issue871：类加载器一问的补充说明）：
    自定义加载器的话，需要继承 ClassLoader 。如果我们不想打破双亲委派模型，就重写 ClassLoader 类中的 findClass() 方法即可，无法被父类加载器加载的类最终会通过这个方法被加载。但是，如果想打破双亲委派模型则需要重写 loadClass() 方法

24. 

25. 了解过JIT么？博客地址：[jit即时编译器](https://blog.csdn.net/sunxianghuang/article/details/52094859)   1.编译器首先分为静态编译和动态编译（jit是动态编译的一种，静态编译就是我们的java编译成class文件）

26. 虚拟机的参数？

    1. -XX:+PrintGCDetails  打印出垃圾回收的详情
    2. JDK1.8 默认使用的是 Parallel Scavenge + Parallel Old，如果指定了-XX:+UseParallelGC  参数，则默认指定了-XX:+UseParallelOldGC，可以使用-XX:-UseParallelOldGC 来禁用该功能
    3. 对象晋升到老年代的年龄阈值，可以通过参数 `-XX:MaxTenuringThreshold` 来设置。
    4. 堆空间调整
    5. -XX:SurvivorRatio=8   eden 和survivor的所占空间大小比例为 8：1
    6. -Xms5m -Xmx5m  初始和最大的堆内存，通常设置成一样的，防止垃圾回收之后有堆抖动的问题
    7. -Xmn10m 新生代的容量
    8. -XX:MetaspaceSize=N //设置 Metaspace 的初始（和最小大小）
       -XX:MaxMetaspaceSize=N //设置 Metaspace 的最大大小