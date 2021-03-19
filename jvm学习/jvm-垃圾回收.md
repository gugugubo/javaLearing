前言：文中出现的示例代码地址为：[gitee代码地址](https://gitee.com/gu_chun_bo/java-construct/tree/master/jvm%E5%AD%A6%E4%B9%A0/jvm)

# 一 JVM垃圾回收模型





## 一. GC算法

### 1.1 标记-清除算法（Mark-Sweep）

算法分为“标记”和“清除”两个阶段首先标记出所有需要回收的对象,然后回收所有需要回收的对象。

问题：效率不高，需要扫描所有的对象，堆越大，GC越慢，并且存在严重的内存碎片问题，空间碎片太多可能会导致后续使用中无法找到足够的连续内存而提前触发另一次的垃圾搜集动作，GC次数越多，碎片越严重。

示例如下，从GCROOT开始找到存活的对象，红色的就是未被标记要回收的，并且红色的区域被回收之后，绿色的还在“原地”，并不会对内存区域进行整理。

![1582787561817](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164655-908837.png)

### 1.2 标记-整理算法（Mark-Compact）

标记过程仍然一样，但后续步骤不是进行直接清理，而是令所有存活的对象一端移动，然后直接清理掉这端边界以外的内存。

![1582786327947](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164628-260953.png)

### 1.3 复制搜集算法（Coping）

#### 以前的复制收集算法

描述：将可用的内存分为两半， 每次只使用其中的一块，当半区内存用完了，仅将还存活的对象复制到另一块上，然后就把原来整块内存空间清理

问题：这样使每次内存回收都是对整个半区的回收，内存分配时就不用考虑内存碎片等复杂情况，**只需要移动堆顶指针**，按顺序分配内存就可以了，实现简单，运行高效，只是这种算法将堆空间内存缩为原来的一半。

#### 现在的复制搜集算法

![1583848735097](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200310215856-280171.png)

描述：现在的商业虚拟机都是采用复制搜集算法来回收**新生代**，将内存分为一块较大的eden空间和两块较小的survivor空间，每次只是用eden和其中一块survivor空间，当回收时将eden和survivor空间中还存活的对象一次性拷贝到另一个survivor空间上，然后清理用过的eden和survivor空间，oracle hotspot虚拟机默认eden 和 survivor的比例是 8:1 ,也就是每次只有百分之十的内存被浪费。示例图如下（最开始A被引用，A引用了C，C引用了H，GC的最后清除了D和G）

![1582787476606](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164632-195319.png)

![1582787495319](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164635-766946.png)

![1582961769604](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164648-218229.png)

![1582961922126](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164650-924632.png)

好处：1.只需要扫描存活的对象，效率更高；2.不会产生碎片 3.复制算法非常适合对象存活时间比较短的对象，因为每次GC总能回收大部分的对象，复制的开销比较小。根据IBM的专门研究，98%的Java对象只会存活1个GC周期，对这些对象很适合用复制算法。而且不用1:1的划分工作区和复制区的空间

问题：复制搜集算法在对象存活率高得时候效率有所下降，就需要有额外的空间进行分配担保用于应付内存中所有对象都百分之百存活的极端情况（在新生代中可以使用老年代进行空间分配担保），所以在老年代不能直接采用这种算法（不是很懂）



### 1.4 分代算法（Generational）

![1582786672708](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164921-907201.png)

描述：当前商业虚拟机的垃圾收集都是采用“分代收集”( Generational Collecting)算法根据对象不同的存活周期将内存划分为几块；一般是把Java堆分作新生代和老年代，这样就可以根据各个年代的特点采用最适当的收集算法，1. 譬如新生代每次GC都有大批对象死去，只有少量存活，那就选用复制算法只需要付出少量存活对象的复制成本就可以完成收集  2. 并且有老年代作为空间分配担保；老年代采用Mark- Sweep或者Mark- Compact算法

年轻代( Young Generation)新生成的对象都放在新生代。年轻代用复制算法进行GC(理论上年轻代对象的生命周期非常短,所以适合复制算法，因为大部分都是不存活的对象)，年轻代分三个区，一个Eden区，两个 Survivor区(可以通过参数设置 Survivor个数)。对象在Eden区中生成，在新生代垃圾回收时，Eden区和From Survivor区中还存活的对象将被复制到另一个 Survivor区(称为To Survivor区)，此次垃圾回收完成之后From Survivor和To Survivor区交换角色。下一次垃圾回收时重复上述过程，直到To Survivor 区被填满，然后一次性将To Survivor中的所有对象移动到老年代中。2个 Survivor是完全对称,轮流替换。Eden和2个 Survivor的缺省比例是8:1:1,也就是10%的空间会被浪费。可以根据 GC log的信息调整大小的比例

老年代( Old Generation)存放了经过一次或多次GC还存活的对象般采用Mark- Sweep或者Mark- Compact算法，进行GC有多种垃圾收集器可以选择。每种垃圾收集器可以看作一个GC算法的具体实现。可以根据具体应用的需求选用合适的垃圾收集器(追求吞吐量?追求最短的响应时间?)





## 二 垃圾回收器

> GC的种类
>
> - Scavenge GC (Minor GC）：对新生代，触发时机是在新对象生成时，Eden空间满了，理论上Eden区大多数对象会在 Scavenge GC回收，复制算法的执行效率会很高， Scavenge GC时间比较短。
> - Full GC：对整个JVM进行整理，包括 Young、Old和Perm（永久代，jdk8没有，jdk为元空间），主要的触发时机:1)Old满了2)Perm满了3) system.gc()   Full GC的执行效率很低，尽量减少 Full GC

分代模型是GC的宏观愿景，垃圾回收器是GC的具体实现，hotspot jvm提供多种垃圾回收器，我们需要根据具体的应用采用多种垃圾回收器

垃圾回收器的并行（Parallel）和并发（Concurrent），并行指的是多个收集器的线程同时工作，但是用户线程处于等待状态；并发指的是收集器在工作的同时，可以允许用户线程工作，但是并发并不代表解决了GC的停顿问题，在关键步骤该停顿的还是要停顿，比如在收集器标记垃圾的时候，但是在清除垃圾的时候用户线程可以和GC线程并发执行

### 2.1 serial 收集器

- 是最早的收集器,单线程收集器，Hotspot Client模式缺省的收集器，收集时会暂停所有工作线程(Stop The World,简称STW)，因为是单线程GC，没有多线程切换的额外开销，简单实用
- New和 Old Generation都可以使用在新生代，采用复制算法，在老年代,采用Mark-Compact算法;
- ![1582791530489](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164918-434894.png)

### 2.2 Serial Old 收集器

​	Serial Old是单线程收集器,使用标记一整理算法, 是老年代的收集器



### 2.3 parnew收集器

- Parnew收集器就是Serial收集器在**新生代**的多线程版本，是Server模式下新生代的缺省收集器，除了使用多个收集线程外,其余行为包括算法、STW、对象分配规则、回收策略等都与 Serial收集器一模一样。

- 使用复制算法(因为针对新生代，效率比较高);

- 只有在多CPU的环境下,效率才会比 Serial收集器高;

- 可以通过`-XX: Parallelg Cthreads`来控制GC线程数的多少，需要结合具体CPU的个数;

  



### 2.4 Parallel Scavenge收集器

Parallel Scavenge 收集器也是一个多线程收集器（Parallel就是并行的意思）,也是使用复制算法，但它的对象分配规则与回收策略都与 Parnew收集器有所不同，它是以吞吐量最大化(即GC时间占总运行时间最小)为目标的收集器实现，它允许较长时间的STW换取总吞吐量最大化，jvm1.8默认在新生代使用Parallel Scavenge ，老年代使用Parallel Old收集



### 2.5 Parallel Old 收集器

JVM1.6提供，在此之前，新生代使用PS收集器的话，老年代除了使用Serial Old外别无选择，因为PS无法和CMS配合工作。jvm1.8默认在新生代使用Parallel Scavenge ，老年代使用Parallel Old收集

- Parallel Scavenge在**老年代**的实现；
- 采用多线程,Mark-Compact算法；
- 更注重吞吐量Parallel Scavenge+ Parallel Old = 高吞吐量,但GC停顿可能不理想
- ![1582792519271](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164916-517287.png)



### 2.6 CMS收集器

CMS是一种以最短停顿时间为目标的**老年代**收集器，使用CMS并不能达到GC效率最高（总的GC时间最小），但是它能尽可能降低服务的停顿时间

- 只针对老年区,一般在新生代结合Parnew使用
- CMS收集器使用的是标记--清除算法
- 使用-XX:+ UseConcMarkSweepGC打开

#### 收集步骤方法一

CMS是基于“**标记--清除**”算法实现的，在老年代中的整个过程分为4个步骤：

- 其中，初始标记，重新标记这两个步骤任然需要“stop the world”，其它两个步骤中用户线程是一起并发执行的
- 初始标记（CMS initial mark），初始标记只是标记一下GC ROOTS 能直接关联到的对象，速度很快
- 并发标记（CMS concurrent mark），并发标记阶段就是进行GC ROOTS Tracing 的过程，此时用户线程也是在同步执行的
- 重新标记（CMS remark），重新标记阶段则是为了修正并发标记期间因为用户程序继续运作而导致标记产生变动的那一部分对象的标记记录（ 这部分对象是指从 GC Roots 不可达的对象，因为用户程序的并发运行，又可达了），这个阶段的停顿时间一般会比初始标记阶段稍长一些，但是远比并发标记的时间短。
- 并发清除（CMS concurrent sweep），收集在标记阶段被标识为不可访问的对象。The collection of a dead object adds the space for the object to a free list for later allocation. Coalescing of dead objects may occur at this point. Note that live objects are not moved.死亡对象收集为空闲列表增加了更多的空间，以便以后分配。在这一点上可能会发生死物体空间的的合并。请注意，不会移动活动对象。
- CMS收集器的运作步骤如下图所示，在整个过程中耗时最长的并发标记和并发清除过程收集器线程都可以和用户线程一起工作，因此从整体上看，CMS收集器线程的内存回收过程是与用户线程一起并发执行的。
  - ![1582892656658](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164913-9221.png)

#### CMS缺点

- CMS以牺牲CPU资源的代价来减少用户线程的停顿。当CPU个数少于4的时候,有可能对吞吐量影响非常大；
- [CMS收集器无法处理浮动垃圾（Floating Garbage）](https://www.cnblogs.com/caophoenix/p/13218466.html),即第一次标记，认为某个对象不是垃圾，但是在CMS线程和用户线程在并发执行的过程中此对象可能变成了垃圾，那么CMS无法在这次的垃圾回收中将它回收掉。无法处理这些垃圾可能出现”concurrent mode failure“失败而导致另一次Full GC的产生。如果在应用中老年代的增长速度不是太快，可以适当调高`-XX:CMSInitiatingOccupancyFractio` 的值来提高触发的百分比(不是很懂)，以便降低内存回收的次数从而获取更好的性能。要是CMS运行期预留的内存无法满足程序的需要时，虚拟机将启动后备预案，临时启用Serial Old收集器来重新进行老年代的垃圾收集，这样一来停顿的时间就更久了。所以说参数`-XX:CMSInitiatingOccupancyFraction` 设置太高容易导致大量的”concurrent mode failure“失败，性能反而降低
- 由于基于MS算法即`Mark-Sweep`，收集结束时会带来碎片问题，空间碎片过多会给大对象分配带来很大麻烦，望往往出现老年代还有很大的空间剩余，但是无法找到足够大的连续空间来分配当前对象，不得不提前进行一次Full GC。CMS收集器提供了一个参数：`-XX:+UseCMSCompactAtFullConnection` 开关参数，默认是开启的，用于在CMS收集器顶不住要进行 Full GC时同时开启内存碎片的合并整理过程，内存整理的过程是无法并发的并且需要stw，空间碎片问题没有了，但停顿时间不得不变长。  



#### 收集步骤方法二

CMS收集器收集步骤，以下是将上面的四个步骤进一步细分为7个步骤，但是其中有stw的还是只有两个步骤，减少了stw的时间。

- Phase 1: Initial Mark，这个是CMS两次stop-the-world事件的其中一次,这个阶段的目标是:标记那些直接被GCroot引用或者被年轻代存活对象所引用的所有对象（CMS是针对老年代的）
  - ![1583929967459](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200311203248-360957.png)
- Phase 2：Concurrent Mark，在这个阶段 Garbage Collector会遍历老年代,然后标记所有存活的对象,它会根据上个阶段找到的 GC Roots遍历査找。并发标记阶段，它会与用户的应用程序并发运行并不是老年代所有的存活对象都会被标记，因为在标记期间用户的程序可能会改变一些引用。在下的图中,与阶段1的图进行对比,就会发现有一个对象的引用已经发生了变化
  - ![1582896958721](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164910-509868.png)
- Phase 3: Concurrent Preclean，这也是一个并发阶段,与应用的线程并发运行,并不会stop用户线程。在并发运行的过程中,一些对象的引用可能会发生变化,但是这种情况发生时,JVM会将包含这个对象的区域(Card)标记为Diy，这个动作称为Card Marking，在pre-clean阶段，那些能够从Dirty对象到达的对象也会被标记,这个标记做完之后, dirty card标记就会被清除了
  - ![1582940994952](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164710-663159.png)
- Phase 4: Concurrent Abortable Preclean，这也是一个并发阶段,但是同样不会影响用户的应用线程,这个阶段是为了尽量承担STW(stop-the-world)中最终标记阶段的工作。这个阶段持续时间依赖于很多的因素由于这个阶段是在重复做很多相同的工作(比如:重复迭代的次数、完成的工作量或者时钟时间等）
- Phase 5: Final Remark，这是第二个STW阶段,也是CMS中的最后一个，这个阶段的目标是标记老年代所有的存活对象，由于之前的阶段是并发执行的，GC线程可能跟不上应用程序的变化为了完成标记老年代所有存活对象的目标，STW就非常有必要了，这个阶段会比前面的几个阶段更复杂一些
- Phase 6: Concurrent Sweep，这里不需要STW,它是与用户的应用程序并发运行,这个阶段是:清除那些不再使用的对象,回收它们的占用空间为将来使用
  - ![1582941954201](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164851-267408.png)
- Phase 7: Concurrent Reset，这个阶段也是并发执行的,它会重设CMS内部的数据结构,为下次的GC做准备

#### 实验

实验代码MyTest5.java

虚拟机参数

- -verbose:gc
  -Xmx20M
  -Xms20m
  -Xmn10M
  -XX:SurvivorRatio=8
  -XX:+PrintGCDetails
  -XX:+UseConcMarkSweepGC

输出结果

```properties
111111
[GC (Allocation Failure) [ParNew: 6104K->742K(9216K), 0.0029166 secs] 6104K->4840K(19456K), 0.0029703 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2222222
[GC (Allocation Failure) [ParNew: 4995K->65K(9216K), 0.0034135 secs] 9093K->8973K(19456K), 0.0034413 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (CMS Initial Mark) [1 CMS-initial-mark: 8907K(10240K)] 13069K(19456K), 0.0001747 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
#  CMS-initial-mark
[CMS-concurrent-mark-start]
33333333
4444444
[CMS-concurrent-mark: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[CMS-concurrent-abortable-preclean-start]
[CMS-concurrent-abortable-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (CMS Final Remark) [YG occupancy: 6531 K (9216 K)][Rescan (parallel) , 0.0001118 secs][weak refs processing, 0.0000169 secs][class unloading, 0.0003630 secs][scrub symbol table, 0.0006629 secs][scrub string table, 0.0001339 secs][1 CMS-remark: 8907K(10240K)] 15439K(19456K), 0.0013879 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
Heap
 par new generation   total 9216K, used 6531K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
  eden space 8192K,  78% used [0x00000000fec00000, 0x00000000ff250980, 0x00000000ff400000)
  # 新生代存了一个4m和一个2m的数组对象，大概是6m
  from space 1024K,   6% used [0x00000000ff400000, 0x00000000ff4104c8, 0x00000000ff500000)
[CMS-concurrent-sweep-start]
  to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
 concurrent mark-sweep generation total 10240K, used 8907K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
 #  老年代里面是存了两个4m的数组对象，大概是8m
 Metaspace       used 3239K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 352K, capacity 388K, committed 512K, reserved 1048576K

```



### 2.7 总结

HotSpot虚拟机的组成成分

![1582976665504](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164752-567723.png)



## 三 JVM内存分配与回收专题

### 3.1 内存分配

#### 3.1.1 空间分配担保

​	在发生 Minor gc之前，虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象总空间，如果这个条件成立,那么 Minor gc可以确保是安全的。当大量对象在 Minor GC后仍然存活，Survivor区中无法容纳那么多的对象，那么就需要老年代进行空间分配担保，把 Survivor无法容纳的对象提前直接进入老年代；但是如果老年代判断到剩余空间不足(根据以往每一次回收晋升到老年代对象容量的平均值作为经验值)，则进行一次Full GC。



#### 3.1.2 大对象直接进入老年代

大对象就是需要大量连续内存空间的对象（比如：字符串、数组）。

**为什么要这样呢？**

为了避免为大对象分配内存时由于分配担保机制带来的复制而降低效率。



#### 3.1.3 长期存活的对象将进入老年代

既然虚拟机采用了分代收集的思想来管理内存，那么内存回收时就必须能识别哪些对象应放在新生代，哪些对象应放在老年代中。为了做到这一点，虚拟机给每个对象一个对象年龄（Age）计数器。

如果对象在  Eden 出生并经过第一次 Minor GC 后仍然能够存活，并且能被 Survivor 容纳的话，将被移动到 Survivor  空间中，并将对象年龄设为 1，对象在 Survivor 中每熬过一次 MinorGC，年龄就增加 1 岁，当它的年龄增加到一定程度（默认为 15  岁），就会被晋升到老年代中。对象晋升到老年代的年龄阈值，可以通过参数 `-XX:MaxTenuringThreshold` 来设置。

#### 3.1.4 动态对象年龄判定

“Hotspot遍历所有对象时，按照年龄从小到大对其所占用的大小进行累积，当累积的某个年龄大小超过了survivor区的一半时，取这个年龄和MaxTenuringThreshold中更小的一个值，作为新的晋升年龄阈值”。

### 3.2 垃圾回收

#### 3.2.1 垃圾判断算法

##### 引用计数算法（Refesrence Counting）

当对象添加一个引用计数器，当有一个地方以用它，计数器加1，当引用失效，计数器减一，任何时刻计数器为0的对象j就是不可能再被使用。弊端：引用计数器可能无法解决循环引用的问题

##### 根搜索算法( Root Tracing)

在实际生产语言中（java，C#等）都使用根搜索算法判断对象是否存活。算法的基本思路就是通过一系列被称作“GC ROOTS “ 的点作为起始进行向下搜索，当一个对象到GC ROOTS 没有任何引用链相连，则证明此对象是不可用的。

其中的GC ROOTS 包括：

- 在VM栈中（帧中的本地变量）中的引用
- 方法区中的静态引用
- JNI（即一般所说的Native方法）中的引用





#### 3.2.2 垃圾回收期的选择和实现

##### 引用类型

GC要做的是将那些dead对象所占的内存回收掉，hotsopt认为没有引用的对象就是dead。hotspot将引用分成四种，strong，soft，weak，phantom。strong引用是我们最常用到的引用，即默认通过Object o = new Object() 这种方式进行的引用；**soft，weak，Phantom这三种都是继承reference**

在 Full GC时会对 Reference类型的引用进行特殊处理
Soft:内存不够时一定会被GC，长期不用也会被GC 
Weak:一定会被GC
Phantom:本来就没引用,当从 jvm 堆中释放时会通知

具体的对比参考[大佬的文章](https://juejin.im/post/5e65b8096fb9a07cbb6e4a43)

##### GC的时机

在分代模型的基础上,GC从时机上分为两种: Scavenge GC和Full GC

- Scavenge GC (Minor GC）：对新生代，触发时机是在新对象生成时，Eden空间满了，理论上Eden区大多数对象会在 Scavenge GC回收，复制算法的执行效率会很高， Scavenge GC时间比较短。
- Full GC：对整个JVM进行整理，包括 Young、Old和Perm（永久代，jdk8没有，jdk为元空间），主要的触发时机:1)Old满了2)Perm满了3) system.gc()   Full GC的执行效率很低，尽量减少 Full GC

#### 3.2.3 GC时机-线程角度

##### 安全点

> 枚举根节点：当执行系统停顿下来后,并不需要一个不漏地检查完所有执行上下文和全局的引用位置,虚拟机应当是有办法直接得知哪些地方存放着对象引用。在 HotSpot的实现中,是使用一组称为 OopMap的数据结构来达到这个目的的

安全点在 OopMap的协助下， Hotspot可以快速且准确地完成GC Roots枚举，但一个很现实的问题随之而来:很多指令可能导致引用关系变化，或者说引起 OopMap内容变化的指令非常多，如果为每一条指令都生成对应的 OopMap，那将会要大量的额外空间，这样GC的空间成本将会变得更高实际上, Hotspot并没有为每条指令都生成 OopMap，只是在“特定的位置”记录了这些信息,这些位置称为安全点( Safepoint)，即程序执行时并非在所有地方都能停顿下来开始GC，只有在达到安全点时才能暂停。

Safepoint的选定既不能太少以至于让GC等待时间太长，也不能过于频繁以至于过分增大运行时的负载。所以，安全点的选定基本上是以“是否具有让程序长时间执行的特征”为标准进行选定的，因为每条指令执行的时间非常短暂，程序不太可能因为指令流长度太长这个原因而过长时间运行，“长时间执行”的最明显特征就是指令序列复用，例如方法调用、循环跳转、异常跳转等，所以具有这些功能的指令オ会产生 Safepoint(不是很懂)

对于 Safepoint，另一个需要考虑的问题是如何在GC发生时让所有线程(这里不包括执行JNI调用的线程)都“跑”到最近的安全点再停顿下来:抢占式中断( Preemptive Suspension)和主动式中断(Voluntary Suspension)

###### 抢占式中断

抢占式中断:它不需要线程的执行代码主动去配合,在GC发生时,首先把所有线程全部中断,如果有线程中断的地方不在安全点上,就恢复线程,让它“跑”到安全点上。

###### 主动式中断

主动式中断:当GC需要中断线程的时候，不直接对线程操作，仅仅简单地设置一个标志，各个线程执行时主动去轮询这个标志，发现中断标志为真时就自己中断挂起。注意：这个轮询也只是在轮询也只是在指定的地方才进行轮询，标志的地方和安全点是重合的。现在几乎没有虚拟机采用抢占式中断来暂停线程从而响应GC事件。

##### 安全区域

在使用 SafePoint似乎已经完美地解决了如何进入GC的问题,但实际上情况却并不一定。 SafePoint机制保证了程序执行时,在不太长的时间内就会遇到可进入GC的Safepoint。但如果程序在“不执行”的时候呢?所谓程序不执行就是没有分配CPU时间,典型的例子就是处于Sleep状态或者 Blocked状态,这时候线程无法响应JVM的中断请求,JVM也显然不太可能等待线程重新分配CPU时间。对于这种情况,就需要安全区域(SafeRegion)来解决了。

在线程执行到 Safe Region中的代码时，首先标识自己已经进入了 Safe Region，那样,当在这段时间里JVM要发起GC时,就不用管标识自己为 Safe Region状态的线程了。在线程要离开 Safe Region时，它要检査系统是否已经完成了根节点枚举(或者是整个GC过程)，如果完成了，那线程就继续执行，否则它就必须等待直到收到可以安全离开 Safe Region的信号为止。

### 3.3 一些感悟

#### 3.3.1 内存泄露的经典原因

###### 对象定义在错误的范围( Wrong Scope)；

![1582794952530](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164846-878102.png)

###### 异常( Exception)处理不当

![1582795049517](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164816-77853.png)

![1582795085858](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164833-867525.png)

###### 集合数据管理不当

![1582795166640](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164712-132297.png)





## 四 参数与实验

- -verbose gc 打印出垃圾回收的详情

- -XX:+PrintGCDetails

- -XX:+PrintGCDateStamps   打印出gc的时间戳

- -XX:+PrintCommandLineFlags  在命令行打印出虚拟机的参数、

- 堆空间调整

- -XX:SurvivorRatio=8   eden 和survivor的所占空间大小比例为 8：1

- -Xms5m -Xmx5m  初始和最大的堆内存，通常设置成一样的，防止垃圾回收之后有堆抖动的问题

- -Xmn10m 新生代的容量

- 新生代晋升老年代相关

- -XX:PretenureSizeThreshold=4194304  （Tenured是老年代的意思）当创建的对象的大小已经超过这个值，那么此对象不会被放到新生代中，而是直接在老年代中。 此参数需要和参数 -XX:+UseSerialGC一起使用(虚拟机运行在 Client模式下的默认值，打开此开关后，使用Serial old的收集器组合进行内存回收)

- -XX:MaxTenuringThreshold=5  （Threshold是门槛的意思）设置对象晋升的到老年代对象年龄阈值的最大值，即虽然可以jvm一般是自动调节回收对象的回收年龄，但是也不能超过此值。此值的默认值是15，CMS中的默认值是6，G1中的默认值是15。

  - 经历过多次GC后，存活的对象会在From Survivor 与 To Survivor 之间来回存放，而这里面的一个前提是有足够的空间来存放这些数据，在GC算法中，会计算每个对象年龄的大小，如果某个年龄总的大小已经大于survivor空间的百分之五十，那么这时就需要调整阈值，取这个年龄和MaxTenuringThreshold中更小的一个值，作为新的晋升年龄阈值，不能再继续等到默认的15次才完成晋升，因为不调整会导致survivor的空间不足，所以需要调整阈值，让这些存活的对象尽快完成晋升。

- -XX:TargetSurvivorRatio=60 设置survivor空间的占比达到百分之六十时就进行一次对象晋升

- -XX:+PrintTenuringDistribution  （Distribution是分配的意思）打印出各年龄阶段的对象的占有内存

- ![1582814508681](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164720-262990.png)

  

### 实验一

输入虚拟机参数执行MyTest1.java

- -XX:SurvivorRatio=8 -Xms20m -Xmx20m -Xmn10m -verbose：gc -XX:+PrintGCDetails

```properties
[GC (Allocation Failure) [PSYoungGen: 8192K->1016K(9216K)] 8192K->5193K(19456K), 0.0066065 secs] [Times: user=0.08 sys=0.00, real=0.01 secs] 
# [GC (Allocation Failure)   GC 代表的是一次Minor GC 
#  [PSYoungGen: 8192K->1016K(9216K)] 代表新生代垃圾回收之前是8192k，回收之后是1016k，总的内存是9216k,即9m（这里是eden和from survivor区域相加的结果），PSYoungGen中的PS代表Parallel Scavenge,这是默认使用的收集器
# 8192K->5193K(19456K) 代表的是在执行回收之前总的堆的大小为8192K，回收之后是5193K，而总的堆的可用容量是19456K（这里是去掉了一个to survivor 区域的大小）
# 0.0066065 secs 代表执行这次垃圾回收的时间是0.0066065 秒
# [Times: user=0.08 sys=0.00, real=0.01 secs]  表示在用户空间用了0.08秒，内核空间用了0.00秒，实际用了0.01秒

[Full GC (Ergonomics) [PSYoungGen: 9208K->0K(9216K)] [ParOldGen: 10232K->9951K(10240K)] 19440K->9951K(19456K), [Metaspace: 3235K->3235K(1056768K)], 0.2093485 secs] [Times: user=0.64 sys=0.00, real=0.21 secs] 
# [Full GC (Ergonomics)  代表这是一次Full GC 
# [PSYoungGen: 9208K->0K(9216K)] 同上，代表新生代回收后的内存为0k，PSYoungGen中的PS代表Parallel Scavenge,这是默认使用的收集器
#  [ParOldGen: 10232K->9951K(10240K)] 表示老年代进行垃圾回收之后的空间，如果这里显示不仅没有变小，而且变大了，其中一个原因是因为部分从新生代晋升到老年代，ParOldGen 中的 ParOld代表Parallel Old,这是默认使用的收集器
# [Metaspace: 3235K->3235K(1056768K)] 代表这次垃圾回收之后，元空间的大小没变
# 以上分析可以说明，jvm1.8默认在新生代使用Parallel Scavenge ，老年代使用Parallel Old收集

Heap
 PSYoungGen      total 9216K, used 435K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
 # PSYoungGen中的PS代表Parallel Scavenge,这是默认使用的收集器
  eden space 8192K, 5% used [0x00000000ff600000,0x00000000ff66cf70,0x00000000ffe00000)
  from space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
  to   space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
 ParOldGen       total 10240K, used 675K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
 # 永久代的内存是10240K，ParOldGen 中的 ParOld代表Parallel Old,这是默认使用的收集器
  object space 10240K, 6% used [0x00000000fec00000,0x00000000feca8ce0,0x00000000ff600000)
 Metaspace       used 3268K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 355K, capacity 388K, committed 512K, reserved 1048576K
```

### 实验二

实验代码为MyTest2.java 虚拟机参数为

- -XX:SurvivorRatio=8
  -Xms20m
  -Xmx20m
  -Xmn10m
  -XX:+PrintGCDetails
  -verbose：gc
  -XX:PretenureSizeThreshold=4194304
  -XX:+UseSerialGC
  - -XX:PretenureSizeThreshold = 4194304  当创建的对象的大小已经超过这个值，那么此对象不会被放到新生代中，而是直接在老年代中 此参数需要和 参数 -XX:+UseSerialGC(虚拟机运行在 Client模式下的默认值,打开此开关后,使用SeSerial+ old的收集器组合进行内存回收)一起使用

实验代码为：

```java
    public static void main(String[] args) {
        int size = 1024 * 1024;
        byte[] myAlloc1 = new byte[5 * size];
    }
```

输出结果

```properties
Heap
 def new generation   total 9216K, used 2172K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
  # 此处的新生代称为 def new generation，而不是使用使用Parallel收集器时的 PSYoungGen 
  eden space 8192K,  26% used [0x00000000fec00000, 0x00000000fee1f1b0, 0x00000000ff400000)
  from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
  to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
 tenured generation   total 10240K, used 5120K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
 #  此处的新生代称为  tenured generation，而不是使用使用Parallel收集器时的 ParOldGen  
 # 由于在实验代码中new了一个5m的字节数组，可以看到，字节数组对象被保存到老年代中。
   the space 10240K,  50% used [0x00000000ff600000, 0x00000000ffb00010, 0x00000000ffb00200, 0x0000000100000000)
 Metaspace       used 3236K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 352K, capacity 388K, committed 512K, reserved 1048576K
```





### 实验三

实验参数

- -XX:SurvivorRatio=8
  -Xms20m
  -Xmx20m
  -Xmn10m
  -XX:+PrintGCDetails
  -verbose：gc
  -XX:+PrintCommandLineFlags
  -XX:MaxTenuringThreshold=5
  -XX:+PrintTenuringDistribution

实验代码

```java
    public static void main(String[] args) {
        int size = 1024 * 1024;
        byte[] myAlloc1 = new byte[2 * size];
        byte[] myAlloc2 = new byte[2 * size];
        byte[] myAlloc3 = new byte[2 * size];
        byte[] myAlloc4 = new byte[2 * size];
        System.out.println("完成了");
    }
```

输出结果

```properties
[GC (Allocation Failure) 
Desired survivor size 1048576 bytes, new threshold 5 (max 5)
# Desired survivor size 1048576 bytes 表示现在的survivor空间的大小，即1m；new threshold 5表示当前的jvm动态设置的对象回收的年龄，(max 5)表示回收的年龄阈值
[PSYoungGen: 8152K->840K(9216K)] 8152K->6992K(19456K), 0.0055682 secs] [Times: user=0.03 sys=0.01, real=0.01 secs] 
[Full GC (Ergonomics) [PSYoungGen: 840K->0K(9216K)] [ParOldGen: 6152K->6845K(10240K)] 6992K->6845K(19456K), [Metaspace: 3229K->3229K(1056768K)], 0.0142699 secs] [Times: user=0.03 sys=0.02, real=0.01 secs] 
完成了
Heap
 PSYoungGen      total 9216K, used 2289K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
  eden space 8192K, 27% used [0x00000000ff600000,0x00000000ff83c4a0,0x00000000ffe00000)
  from space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
  to   space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
 ParOldGen       total 10240K, used 6845K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
  object space 10240K, 66% used [0x00000000fec00000,0x00000000ff2af728,0x00000000ff600000)
 Metaspace       used 3237K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 352K, capacity 388K, committed 512K, reserved 1048576K

```



### 实验四

虚拟机参数

- -verbose：gc
  -Xmx200m
  -Xmn50m
  -XX:TargetSurvivorRatio=60
  -XX:+PrintTenuringDistribution
  -XX:+PrintGCDetails
  -XX:+PrintGCDateStamps
  -XX:+UseParNewGC
  -XX:+UseConcMarkSweepGC
  -XX:MaxTenuringThreshold=3

实验代码 MyTest4.java

输出结果

```properties
2020-02-28T16:13:31.886+0800: [GC (Allocation Failure) 2020-02-28T16:13:31.886+0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 3 (max 3)
- age   1:    2818384 bytes,    2818384 total
: 40349K->2785K(46080K), 0.0016338 secs] 40349K->2785K(109568K), 0.0017041 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
11111111
# Desired survivor size 3145728 bytes ,其中3145728 bytes的值就是3m，是survivor乘于0.6算出来的（-XX:TargetSurvivorRatio=60），当survivor超过此大小时就会重新动态设置threshold的值，但是也不会超过3.
2020-02-28T16:13:32.891+0800: [GC (Allocation Failure) 2020-02-28T16:13:32.891+0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 2 (max 3)
- age   1:     342328 bytes,     342328 total
- age   2:    2866552 bytes,    3208880 total
: 42918K->3328K(46080K), 0.0016638 secs] 42918K->3328K(109568K), 0.0017198 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
222222222
2020-02-28T16:13:33.896+0800: [GC (Allocation Failure) 2020-02-28T16:13:33.896+0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 3 (max 3)
- age   1:         64 bytes,         64 total
- age   2:     341632 bytes,     341696 total
: 43843K->858K(46080K), 0.0034513 secs] 43843K->3621K(109568K), 0.0034982 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
3333333333
2020-02-28T16:13:34.905+0800: [GC (Allocation Failure) 2020-02-28T16:13:34.905+0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 3 (max 3)
- age   1:         64 bytes,         64 total
- age   2:         64 bytes,        128 total
- age   3:     341856 bytes,     341984 total
: 41579K->484K(46080K), 0.0006988 secs] 44342K->3247K(109568K), 0.0007548 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
4444444444
2020-02-28T16:13:35.908+0800: [GC (Allocation Failure) 2020-02-28T16:13:35.908+0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 1 (max 3)
- age   1:    3145840 bytes,    3145840 total
- age   2:         64 bytes,    3145904 total
- age   3:         64 bytes,    3145968 total
: 41211K->3153K(46080K), 0.0016465 secs] 43974K->6250K(109568K), 0.0016891 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
5555555
2020-02-28T16:13:36.912+0800: [GC (Allocation Failure) 2020-02-28T16:13:36.912+0800: [ParNew
Desired survivor size 3145728 bytes, new threshold 3 (max 3)
- age   1:         56 bytes,         56 total
: 43885K->20K(46080K), 0.0020702 secs] 46982K->6189K(109568K), 0.0021193 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
666666
Heap
 par new generation   total 46080K, used 19046K [0x00000000f3800000, 0x00000000f6a00000, 0x00000000f6a00000)
  eden space 40960K,  46% used [0x00000000f3800000, 0x00000000f4a947a0, 0x00000000f6000000)
  from space 5120K,   0% used [0x00000000f6000000, 0x00000000f6005130, 0x00000000f6500000)
  to   space 5120K,   0% used [0x00000000f6500000, 0x00000000f6500000, 0x00000000f6a00000)
 concurrent mark-sweep generation total 63488K, used 6169K [0x00000000f6a00000, 0x00000000fa800000, 0x0000000100000000)
 Metaspace       used 3748K, capacity 4536K, committed 4864K, reserved 1056768K
  class space    used 412K, capacity 428K, committed 512K, reserved 1048576K

```





