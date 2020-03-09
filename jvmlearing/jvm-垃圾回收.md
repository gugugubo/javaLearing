# JVM垃圾回收模型



## 垃圾判断算法

### 引用计数算法（Refesrence Counting）

当对象添加一个引用计数器，当有一个地方以用它，计数器加1，当引用失效，计数器减一，任何时刻计数器为0的对象j就是不可能再被使用。弊端：引用计数器可能无法解决循环引用的问题

### 根搜索算法( Root Tracing)

在实际生产语言中（java，C#等）都使用跟搜索算法判断对象是否存活。算法的基本思路就是通过一系列被称作“GC ROOTS “ 的点作为起始进行向下搜索，当一个对象到GC ROOTS 没有任何引用链相连，则证明此对象是不可用的。

其中的GC ROOTS 包括：

- 在VM栈中（帧中的本地变量）中的引用
- 方法区中的静态引用
- JNI（即一般所说的Native方法）中的引用









## GC算法

### 标记-清除算法（Mark-Sweep）

算法分为“标记”和“清除”两个阶段首先标记出所有需要回收的对象,然后回收所有需要回收的对象。

问题：效率不高，需要扫描所有的对象，堆越大，GC越慢，并且存在严重的内存碎片问题,空间碎片太多可能会导致后续使用中无法找到足够的连续内存而提前触发另一次的垃圾搜集动作。GC次数越多，碎片越严重

示例如下，红色的就是未被标记要回收的。

![1582787561817](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164655-908837.png)

### 标记-整理算法（Mark-Compact）

标记过程仍然一样,但后续步骤不是进行直接清理,而是令所有存活的对象一端移动,然后直接清理掉这端边界以外的内存。

![1582786327947](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164628-260953.png)

### 复制搜集算法（Coping）

#### 以前的复制收集算法

描述：将可用的内存分为两半， 每次只使用其中的一块，当半区内存用完了，仅将还存活的对象复制到另一块上，然后就把原来整块内存空间清理

问题：这样使每次内存回收都是对整个半区的回收，内存分配时就不用考虑内存碎片等复杂情况，**只需要移动堆顶指针**，按顺序分配内存就可以了，实现简单，运行高效，只是这种算法将堆空间内存缩为原来的一半。

#### 现在的复制搜集算法

描述：现在的商业虚拟机都是采用复制搜集算法来回收**新生代**，将内存分为一块较大的eden空间和两块较小的survivor空间，每次只是用eden和其中一块survivor空间，当回收时将eden和survivor空间中还存活的对象一次性拷贝到另一个survivor空间上，然后清理用过的eden和survivor空间，oracle hotspot虚拟机默认eden 和 survivor的比例是 8:1 ,也就是每次只有 百分之十的内存被浪费。示例图如下（最开始A被引用，A引用了C，C引用了H，最后清除了D和G），注意这里跟前面的标记整理和标记清除不一样哦，这里是不用标记的。

![1582787476606](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164632-195319.png)

![1582787495319](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164635-766946.png)

![1582961769604](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164648-218229.png)

![1582961922126](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164650-924632.png)

好处：1.只需要扫描存活的对象（跟前面的标记整理和标记清除不一样哦），效率更高；2.不会产生碎片 3.复制算法非常适合对象存活时间比较短的对象，因为每次GC总能回收大部分的对象，复制的开销比较小。根据IBM的专门研究,98%的Java对象只会存活1个GC周期,对这些对象很适合用复制算法。而且不用1:1的划分工作区和复制区的空间

问题：复制搜集算法在对象存活率高得时候效率有所下降，就需要有额外的空间进行分配担保用于应付半区内存中所有对象都百分之百存活的极端情况，所以在老年代不能直接采用这种算法

#### 空间分配担保

在发生 Minor gc之前,虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象总空间,如果这个条件成立,那么 Minor gc可以确保是安全的。当大量对象在 Minor GC后仍然存活,就需要老年代进行空间分配担保,把 Survivor无法容纳的对象直接进入老年代。如果老年代判断到剩余空间不足(根据以往每一次回收晋升到老年代对象容量的平均值作为经验值),则进行一次Full GC。

### 分代算法（Generational）

![1582786672708](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164921-907201.png)

描述：当前商业虚拟机的垃圾收集都是采用“分代收集”( Generational Collecting)算法根据对象不同的存活周期将内存划分为几块；一般是把Java堆分作新生代和老年代,这样就可以根据各个年代的特点采用最适当的收集算法,1. 譬如新生代每次GC都有大批对象死去,只有少量存活,那就选用复制算法只需要付出少量存活对象的复制成本就可以完成收集  2. 并且有老年代作为空间分配担保；老年代采用Mark- Sweep或者Mark- Compact算法

年轻代( Young Generation)新生成的对象都放在新生代。年轻代用复制算法进行GC(理论上年轻代对象的生命周期非常短,所以适合复制算法)年轻代分三个区。一个Eden区,两个 Survivor区(可以通过参数设置 Survivor个数)。对象在Eden区中生成。当Eden区满时,还存活的对象将被复制到一个 Survivor区,当这个 Survivor区满时,此区的存活对象将被复制到另外一个 Survivor区,当第二个 Survivor区也满了的时候,从第一个 Survivor区复制过来的并且此时还存活的对象,将被复制到老年代。2个 Surviⅳor是完全对称,轮流替换。Eden和2个 Survivor的缺省比例是8:1:1,也就是10%的空间会被浪费。可以根据 GC log的信息调整大小的比例

老年代( Old Generation)存放了经过一次或多次GC还存活的对象般采用Mark- Sweep或者Mark- Compact算法，进行GC有多种垃圾收集器可以选择。每种垃圾收集器可以看作一个GC算法的具体实现。可以根据具体应用的需求选用合适的垃圾收集器(追求吞吐量?追求最短的响应时间?)





## 垃圾回收期的选择和实现

### 引用类型

GC要做的是将那些dead对象所占的内存回收掉，hotsopt认为没有引用的对象就是dead。hotspot将引用分成四种，strong，soft，weak，phantom。strong即默认通过Object o = new Object() 这种方式进行的引用；**soft，weak，Phantom这三种都是继承reference**

在 Full GC时会对 Reference类型的引用进行特殊处理
Soft:内存不够时一定会被GC：长期不用也会被GC 
Weak:一定会被GC,当被mark为dead,会在 Reference Queue中通知；
Phantom:本来就没引用,当从 jvm heap中释放时会通知



### GC的时机

在分代模型的基础上,GC从时机上分为两种: Scavenge GC和Full GC

- Scavenge GC (Minor GC）：对新生代，触发时机:新对象生成时,Eden空间满了，理论上Eden区大多数对象会在 Scavenge GC回收,复制算法的执行效率会很高, Scavenge GC时间比较短。
- Full GC：对整个JVM进行整理,包括 Young、Old和Perm（永久代，jdk8没有，jdk为元空间）,主要的触发时机:1)Old满了2)Perm满了3) system.gc()
  效率很低,尽量减少 Full GC



## 垃圾回收器

分代模型是GC的宏观愿景，垃圾回收器是GC的具体实现，hotspot jvm提供多种垃圾回收器，我们需要根据具体的应用采用多种垃圾回收器

垃圾回收器的并行（Parallel）和并发（Concurrent），并行指的是多个收集器的线程同时工作，但是用户线程处于等待状态；并发指的是收集器在工作的同时，可以允许用户线程工作，但是并发并不代表解决了GC的停顿问题，在关键步骤该停顿的还是要停顿，比如在收集器标记垃圾的时候，但是在清除垃圾的时候用户线程可以和GC线程并发执行

### serial 收集器

- 是最早的收集器,单线程收集器,Hotspot Client模式缺省的收集器，收集时会暂停所有工作线程(Stop The World,简称STW)，因为是单线程GC,没有多线程切换的额外开销,简单实用
- New和 Old Generation都可以使用在新生代,采用复制算法，在老年代,采用Mark-Compact算法;
- ![1582791530489](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164918-434894.png)

### Serial Old 收集器

​	Serial Old是单线程收集器,使用标记一整理算法, 是老年代的收集器



### parnew收集器

- Parnew收集器就是Serial收集器在**新生代**的多线程版本，是Server模式下新生代的缺省收集器，除了使用多个收集线程外,其余行为包括算法、STW、对象分配规则、回收策略等都与 Serial收集器一模一样。

- 使用复制算法(因为针对新生代，效率比较高);

- 只有在多CPU的环境下,效率才会比 Serial收集器高;

- 可以通过-XX: Parallelg Cthreads来控制GC线程数的多少，需要结合具体CPU的个数;

  



### Parallel Scavenge收集器

Parallel Scavenge 收集器也是一个多线程收集器（Parallel就是并行的意思）,也是使用复制算法,但它的对象分配规则与回收策略都与 Parnew收集器有所不同,它是以吞吐量最大化(即GC时间占总运行时间最小)为目标的收集器实现,它允许较长时间的STW换取总吞吐量最大化，jvm1.8默认在新生代使用Parallel Scavenge ，老年代使用Parallel Old收集



### Parallel Old 收集器

JVM1.6提供，在此之前，新生代使用PS收集器的话，老年代除了使用Serial Old外别无选择，因为PS无法和CMS配合工作。jvm1.8默认在新生代使用Parallel Scavenge ，老年代使用Parallel Old收集

- Parallel Scavenge在**老年代**的实现；
- 采用多线程,Mark-Compact算法；
- 更注重吞吐量Parallel Scavenge+ Parallel Old = 高吞吐量,但GC停顿可能不理想
- ![1582792519271](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164916-517287.png)



### CMS收集器

CMS是一种以最短停顿时间为目标的**老年代**收集器，使用CMS并不能达到GC效率最高（总的GC时间最小），但是它能尽可能降低服务的停顿时间

- 只针对老年区,一般在新生代结合Parnew使用
- CMS收集器使用的是标记--清除算法
- 使用-XX:+ UseConcMarkSweepGC打开

#### 收集步骤方法一

CMS是基于“**标记--清除**”算法实现的，在老年代中的整个过程分为4个步骤：

- 其中，初始标记，重新标记这两个步骤任然需要“stop the world”，其它两个步骤中用户线程是一起并发执行的
- 初始标记（CMS initial mark），初始标记只是标记一下GC ROOTS 能直接关联到的对象，速度很快
- 并发标记（CMS concurrent mark），并发标记阶段就是进行GC ROOTS Tracing 的过程，此时用户线程也是在同步执行的
- 重新标记（CMS remark），重新标记阶段则是为了修正并发标记期间因为用户程序继续运作而导致标记产生变动的那一部分对象的标记记录，这个阶段的停顿时间一般会比初始标记阶段稍长一些，但是远比并发标记的时间短。
- 并发清除（CMS concurrent sweep），收集在标记阶段被标识为不可访问的对象。The collection of a dead object adds the space for the object to a free list for later allocation. Coalescing of dead objects may occur at this point. Note that live objects are not moved.死对象收集空闲列表增加了更多的空间，以便以后分配。在这一点上可能会发生死物体空间的的合并。请注意，不会移动活动对象。
- CMS收集器的运作步骤如下图所示，在整个过程中耗时最长的并发标记和并发清除过程收集器线程都可以和用户线程一起工作，因此从整体上看，CMS收集器线程的内存回收过程是与用户线程一起并发执行的。
  - ![1582892656658](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164913-9221.png)

#### CMS缺点

- CMS以牺牲CPU资源的代价来减少用户线程的停顿。当CPU个数少于4的时候,有可能对吞吐量影响非常大；
- CMS收集器**无法处理浮动垃圾（Floating Garbage）**,即第一次标记，认为某个对象不是垃圾，但是在CMS线程和用户线程在并发执行的过程中此对象可能变成了垃圾，那么CMS无法在这次的垃圾回收中将它回收掉。无法处理这些垃圾可能出现”concurrent mode failure“失败而导致另一次Full GC的产生。如果在应用中老年代的增长速度不是太快，可以适当调高-XX:CMSInitiatingOccupancyFraction 的值来提高出发的百分比，以便降低内存回收的次数从而获取更好的性能。要是CMS运行期预留的内存无法满足程序的需要时**运行期指的是什么？**，虚拟机将启动后备预案，临时启用Serial Old收集器来重新进行老年代的垃圾收集，这样一来停顿的时间就更久了。所以说参数-XX:CMSInitiatingOccupancyFraction 设置太高容易导致大量的”concurrent mode failure“失败，性能反而降低
- MS即Mark-Sweep,收集结束时会带来碎片问题，空间碎片过多会给大对象分配带来很大麻烦，望往往出现老年代还有很大的空间剩余，但是无法找到足够大的连续空间来分配当前对象，不得不提前进行一次Full GC。CMS收集器提供了一个参数：-XX:+UseCMSCompactAtFullConnection 开关参数，默认是开启的，用于在CMS收集器顶不住要进行 Full GC时同时开启内存碎片的合并整理过程,内存整理的过程是无法并发的并且需要stw,空间碎片问题没有了,但停顿时间不得不变长。  



#### 收集步骤方法二

CMS收集器收集步骤，以下是将上面的四个步骤进一步细分为7个步骤，但是其中有stw的还是只有两个步骤，减少了stw的时间。

- Phase 1: Initial Mark，这个是CMS两次stop-the-world事件的其中一次,这个阶段的目标是:标记那些直接被GCroot引用或者被年轻代存活对象所引用的所有对象（CMS是针对老年代的）
  - ![1582896857132](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164854-655532.png)
- Phase 2 Concurrent Mark，在这个阶段 Garbage Collector会遍历老年代,然后标记所有存活的对象,它会根据上个阶段找到的 GC Roots遍历査找。并发标记阶段,它会与用户的应用程序并发运行并不是老年代所有的存活对象都会被标记,因为在标记期间用户的程序可能会改变一些引用。在下的图中,与阶段1的图进行对比,就会发现有一个对象的引用已经发生了变化
  - ![1582896958721](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164910-509868.png)
- Phase 3: Concurrent Preclean，这也是一个并发阶段,与应用的线程并发运行,并不会stop用户线程。在并发运行的过程中,一些对象的引用可能会发生变化,但是这种情况发生时,JVM会将包含这个对象的区域(Card)标记为Diy,这也就是 Card Marking在pre-clean阶段,那些能够从Dirty对象到达的对象也会被标记,这个标记做完之后, dirty card标记就会被清除了
  - ![1582940994952](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164710-663159.png)
- Phase 4: Concurrent Abortable Preclean
  - 这也是一个并发阶段,但是同样不会影响用户的应用线程,这个阶段是为了尽量承担STW(stop-the-world)中最终标记阶段的工作。这个阶段持续时间依赖于很多的因素由于这个阶段是在重复做很多相同的工作,直接满足一些条件(比如:重复迭代的次数、完成的工作量或者时钟时间等
- Phase 5: Final Remark，这是第二个STW阶段,也是CMS中的最后一个,这个阶段的目标是标记老年代所有的存活对象,由于之前的阶段是并发执行的,GC线程可能跟不上应用程序的变化为了完成标记老年代所有存活对象的目标,STW就非常有必要了。通常CMS的 Final Remark阶段会在年轻代尽可能干净的时候运行,目的是为了减少连续STW发生的可能性(年轻代存活对象过多的话,也会导致老年代涉及的存活对象会很多)。这个阶段会比前面的几个阶段更复杂一些
- Phase 6: Concurrent Sweep，这里不需要STW,它是与用户的应用程序并发运行,这个阶段是:清除那些不再使用的对象,回收它们的占用空间为将来使用
  - ![1582941954201](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164851-267408.png)
- Phase 7: Concurrent Reset，这个阶段也是并发执行的,它会重设CMS内部的数据结构,为下次的GC做准备

#### 实验

实验代码MyTest5.java

虚拟机参数

- -verbose：gc
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







### 枚举根节点

当执行系统停顿下来后,并不需要一个不漏地检查完所有执行上下文和全局的引用位置,虚拟机应当是有办法直接得知哪些地方存放着对象引用。在 HotSpot的实现中,是使用一组称为 OopMap的数据结构来达到这个目的的

### 安全点

安全点在 OopMap的协助下, Hotspot可以快速且准确地完成GC Roots枚举,但一个很现实的问题随之而来:很多指令可能导致引用关系变化,或者说引起 OopMap内容变化的指令非常多,如果为每一条指令都生成对应的 OopMap,那将会要大量的额外空间,这样GC的空间成本将会变得更高实际上, Hotspot并没有为每条指令都生成 OopMap,只是在“特定的位置”记录了这些信息,这些位置称为安全点( Safepoint),即程序执行时并非在所有地方都能停顿下来开始GC,只有在达到安全点时才能暂停。

Safepoint的选定既不能太少以至于让GC等待时间太长,也不能过于频繁以至于过分增大运行时的负载。所以,安全点的选定基本上是以“是否具有让程序长时间执行的特征”为标准进行选定的因为每条指令执行的时间非常短暂,程序不太可能因为指令流长度太长这个原因而过长时间运行,“长时间执行”的最明显特征就是指令序列复用,例如方法调用、循环跳转、异常跳转等,所以具有这些功能的指令オ会产生 Safepoint

对于 Savepoint,另一个需要考虑的问题是如何在GC发生时让所有线程(这里不包括执行JNI调用的线程)都“跑”到最近的安全点再停顿下来:抢占式中断( Preemptive Suspension)和主动式中断(Voluntary Suspension)

#### 抢占式中断

抢占式中断:它不需要线程的执行代码主动去配合,在GC发生时,首先把所有线程全部中断,如果有线程中断的地方不在安全点上,就恢复线程,让它“跑”到安全点上。

#### 主动式中断

主动式中断:当GC需要中断线程的时候,不直接对线程操作,仅仅简单地设置一个标志,各个线程执行时主动去轮询这个标志,发现中断标志为真时就自己中断挂起。注意：这个轮询也只是在轮询也只是在指定的地方才进行轮询，标志的地方和安全点是重合的或者是创建对象需要分配内存的地方。现在几乎没有虚拟机采用抢占式中断来暂停线程从而响应GC事件。

### 安全区域

在使用 SafePoint似乎已经完美地解决了如何进入GC的问题,但实际上情况却并不一定。 SafePoint机制保证了程序执行时,在不太长的时间内就会遇到可进入GC的Safepoint。但如果程序在“不执行”的时候呢?所谓程序不执行就是没有分配CPU时间,典型的例子就是处于Sleep状态或者 Blocked状态,这时候线程无法响应JVM的中断请求,JVM也显然不太可能等待线程重新分配CPU时间。对于这种情况,就需要安全区域(SafeRegion)来解决了。

在线程执行到 Safe Region中的代码时,首先标识自己已经进入了 Safe Region,那样,当在这段时间里JVM要发起GC时,就不用管标识自己为 Safe Region状态的线程了。在线程要离开 Safe Region时,它要检査系统是否已经完成了根节点枚举(或者是整个GC过程),如果完成了,那线程就继续执行,否则它就必须等待直到收到可以安全离开 Safe Region的信号为止。



### 内存泄露的经典原因

#### 对象定义在错误的范围( Wrong Scope)；

![1582794952530](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164846-878102.png)

#### 异常( Exception)处理不当

![1582795049517](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164816-77853.png)

![1582795085858](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164833-867525.png)

#### 集合数据管理不当

![1582795166640](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164712-132297.png)





## 参数

- -verbose gc 打印出垃圾回收的详情

- -XX:+PrintGCDetails

- -XX:+PrintGCDateStamps   打印出gc的时间戳

- -XX:+PrintCommandLineFlags  在命令行打印出虚拟机的参数、

- 堆空间调整

- -XX:SurvivorRatio=8   eden 和survivor的所占空间大小比例为 8：1

- -Xms5m -Xmx5m  初始和最大的堆内存，通常设置成一样的，防止垃圾回收之后有堆抖动的问题

- -Xmn10m 新生代的容量

- 新生代晋升老年代相关

- -XX:PretenureSizeThreshold=4194304  （Tenured是老年代的意思）当创建的对象的大小已经超过这个值，那么此对象不会被放到新生代中，而是直接在老年代中。 此参数需要和参数 -XX:+UseSerialGC一起使用(虚拟机运行在 Client模式下的默认值,打开此开关后,使用Serial old的收集器组合进行内存回收)

- -XX:MaxTenuringThreshold=5  （Threshold是门槛的意思）设置对象晋升的到老年代对象年龄阈值的最大值，即虽然可以jvm一般是自动调节回收对象的回收年龄，但是也不能超过此值。此值的默认值是15，CMS中的默认值是6，G1中的默认值是15。

  - 经历过多次GC后，存活的对象会在From Survivor 与 To Survivor 之间来回存放，而这里面的一个前提是有足够的空间来存放这些数据，在GC算法中，会计算每个对象年龄的大小，如果达到某个年龄之后发现总的大小已经大于survivor空间的百分之五十，那么这时就需要调整阈值，不能再继续等到默认的15次才完成晋升，因为不调整会导致survivor的空间不足，所以需要调整阈值，让这些存活的对象尽快完成晋升。

- -XX:TargetSurvivorRatio=60 设置survivor空间的占比达到百分之六十时就进行一次对象晋升

- -XX:+PrintTenuringDistribution  （Distribution是分配的意思）打印出各年龄阶段的对象的占有内存

  

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

实验代码



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



## 杂谈

![1582814508681](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164720-262990.png)

# G1收集器

- g1收集器是一个面向服务端的垃圾收集器适用于多核处理器、大内存容量的服务端系统。
- 它满足短时间gc停顿的同时达到一个较高的吞吐量。
- JDK7以上版本适用

先介绍两个概念：吞吐量和响应能力，响应能力和吞吐量是评价一个系统的两个重要指标

### 吞吐量

- 吞吐量关注的是,在一个指定的时间内,最大化一个应用的工作量。
- 如下方式来衡量一个系统吞吐量的好坏：
  - 在一定时间内同一个事务(或者任务、请求)完成的次数(tps)
  - 数据库一定时间以完成多少次查询
- 对于关注吞吐量的系统,一定次数卡顿（即stw）是可以接受的,因为这个系统关注长时间的大量任务的执行能力，单次快速的响应并不值得考虑

### 响应能力

响应能力指一个程序或者系统对请求是否能够及时响应,比如:

- 一个桌面UI能多快地响应一个事件
- 一个网站能够多快返回一个页面请求
- 数据库能够多快返回查询的数据

对于这类对响应能力敏感的场景,长时间的停顿是无法接受的，关注每一次反应的能力。



## G1收集器的设计目标

与应用线程同时工作,几乎不需要 stop the work(与CMS类似);

- G1的设计规划是要替换掉CMS，G1在某些方面弥补了CMS的不足，比如CMS使用的是mark-sweep算法，自然会产生内存碎片(CMS只能在Full GC时,用 stop the world整理内存碎片)，然而G1基于copying算法，高效的整理剩余内存，而不需要管理内存碎片
- GC停顿更加可控，甚至可以设置一个时间阈值，比如说可以回收某些部分的老年代，而不像CMS老年代必须全部回收（其它收集器时间可能难以把握）
- **gc不要求额外的内存空间(CMS需要预留空间存浮动垃圾);**

## HotSpot虚拟机的组成成分

![1582976665504](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164752-567723.png)

## G1收集器的堆结构

![1582976958190](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164749-948614.png)



## G1重要概念

### 分区( Region)

- G1采取了不同的策略来解决并行、串行和CMS收集器的碎片、暂停时间不可控等问题—G1将整个堆分成相同大小的分区或称为区域( Region）

  - ![1582979735814](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164747-576746.png)
  - 每个分区都可能是年轻代也可能是老年代,但是在同一时刻只能属于某个代。年轻代，survivor区，老年代这些概念还存在,成为逻辑上的概念,这样方便复用之前分代框架的逻辑。分区在物理上不需要连续,则带来了额外的好处,可以在**老年代**中只对某些区域（Region）进行回收。对于**新生代**依然是在新生代满了的时候,对整个新生代进行回收一一整个新生代中的对象,要么被回收、要么晋升,至于新生代也采取分区机制的原因,则是因为这样跟老年代的策略统一,方便调整代的大小【其实新生代并不适合这样的垃圾收集，因为是对新生代的所有区域进行回收！不用像老年代那样选择回收效益高的Region】
  - 在G1中,还有一种特殊的区域,叫 Humongous区域。如果一个对象占用的空间达到或是超过了分区容量50%以上,G1收集器就认为这是一个巨型对象。这些巨型对象,默认直接会被分配在老年代,但是如果它是一个短期存在的巨型对象就会对垃圾收集器造成负面影响。为了解决这个问题,G1划分了一个 Humongous区,它用来专门存放巨型对象。如果一个H区装不下一个巨型对象,那么G1会寻找连续的H分区来存储。为了能找到连续的H区,有时候不得不启动Full GC                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           

  

### 收集集合(CSet)

收集集合(Collection Set):一组可被回收的分区的集合，在CSet中存活的数据会在GC过程中被移动到另一个可用分区,CSet中的分区可以来自eden空间、 survivor空间、或者老年代，并且可以同时包含这几代分区的内容。

### 已记忆集合(RSet)

已记忆集合(Remember Set):RSet使区域Region的并行和独立成为可能，RSet记录了其他Region中的对象引用本 Region中对象的关系,属于 points-into结构(谁引用了我的对象)。RSet的价值在于使得垃圾收集器不需要扫描整个堆找到谁引用了当前分区中的对象,只需要扫描RSet即可。

- Region1和 Region3中的对象都引用了Region2中的对象,因此在 Region2的RSet中记录了这两个引用。
- ![1582980273219](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164744-282549.png)

#### point-into Rset解析

G1 GC是在 points-out的 card table之上再加了一层结构来构成 points-into Rset:每个Region会使用point-into Rset记录下到底哪些别的Region有指向自己的指针,而这些指针分别在哪些card的范围内【也就是说 card table 记录自己指向了谁，Point-into Ret 记录了谁指向了自己】。这个RSet其实是一个 hash table,key是别的Region的起始地址,value是一个集合,里面的元素是 card table的 Index.举例来说,如果region A的Rset里有一项的key是 region B,value里有 index为1234的card,它的意思就是 region B的一个card里有引用指向region A。所以对 region A来说该RSet记录的是 points-into的关系，而 card table仍然记录了 points-out的关系。

#### car table 解析

card table是什么又是怎么来的呢？如果一个对象引用的对象很多,赋值器需要对每个引用做处理,赋值器开销会很大,为了解决赋值器开销这个问题在G1中又引入了另外一个概念,卡表( Card Table)。一个 Card Table将一个分区在逻辑上划分为固定大小的连续区域每个区域称之为卡。卡通常较小,介于128到512字节之间。 Card Table通常为字节数组,由Card的索引(即数组下标)来标识每个分区的空间地址

- 默认情况下,每个卡都未被引用。当一个地址空间被引用时,**这个地址空间对应的数组索引的值被标记为0，这里为什么被标记为0呢？,即标记为脏被引用**,此外RSet也将这个数组下标记录下来。一般情况下,这个RSet其实是一个Hash Table,Key是别的 Region的起始地址, Value是一个集合,里面的元素是CardTablel的 Index

### SATB

Snapshot-at-the-beginning(SATB)SATB是G1 GC在全局并发标记阶段使用的增量式的标记算法。并发标记是并发多线程的,但并发线程在同一时刻只扫描一个分区





## GC模式

G1提供了两种GC模式, Young GC和 Mixed Gc两种都是完全 Stop The World的

### Young GC

Young GC:将选择所有**年轻代**里的 Region进行GC（所以CSet里存的就是所有年轻代里面的Region）。将存活的对象复制到幸survivor区是多线程执行的并且需要stw，如果满足晋升到老年代阈值，则某些对象将被提升到老年代，并通过动态计算出下次GC时堆中年轻代的 Region个数,即年轻代内存大小来控制 Young GC的时间开销。                                                                                                   

#### Young GC的触发时机

Young GC在Eden充满时触发,在回收之后所有之前属于Eden的区块全部变成空白即不属于任何一个分区(Eden、 Survivor、Old）

### Mixed GC

Mixed GC:将选择所有**年轻代**里的 Region,外加根据 global concurrent marking统计得出收集收益高的若干**老年代** Region进行GC（所以CSet里存的是所有年轻代里的 Region加上在全局并发标记阶段标记出来的收益高的 Region）。在用户指定的开销目标范围内尽可能选择收益高的老年代 Region，以此来控制Mixed GC的时间开销。

- 需要注意的是：Mixed GC不是 Full GC,它只能回收部分老年代的 Region而不是全部的Region,如果 Mixed GC实在无法跟上程序分配内存的速度,导致老年代填满无法继续进行 Mixed GC,就会退化然后使用 serial old GC( Full GC)来收集整个 GC heap。所以本质上,G1是不提供 Full GC的。所以总的来说，G1的运行过程是这样的:会在 Young GC和Mixed GC之间不断地切换运行,同时定期地做全局并发标记,在实在赶不上对象创建速度的情况下使用Full Gc( Serial GC)。
- **它的GC步骤分为两步全局并发标记(global concurrent marking)和拷贝存活对象(evacuation)**



#### Mixed gc 中的Global  concurrent marking

Mixed gc 中的 global concurrent marking的执行过程类似于CMS,但是不同的是,在G1 GC中,它主要是为 Mixed GC提供标记服务的【老年代之所以知道为什么哪里的回收效益高就是根据此服务得出的结果确定的】,并不是一次GC过程的一个必须环节。global concurrent marking的执行过程分为四个步骤

- 初始标记( initial mark,STW):它标记了从GC Root开始直接可达的对象。第一阶段 initial mark是共用了 Young GC的暂停,这是因为他们可以复用 root scan操作,所以可以说 global concurrent marking是伴随 Young GC而发生的。
- 并发标记( Concurrent Marking):这个阶段从GC Root开始对heap中的对象进行标记,标记线程与应用程序线程并发执行,并且收集各个Region的存活对象信息。
- 重新标记( Remark,STW) :标记那些在并发标记阶段发生变化的对象,将被回收。
- 清理( Cleanup ):清除空 Region(没有存活对象的),加入到 free list。第四阶段 Cleanup只是回收了没有存活对象的 Region,所以它并不需要STW

#### 停顿预测模型

- G1收集器突出表现出来的一点是通过一个停顿预测模型根据用户配置的停顿时间来选择CSet的大小,从而达到用户期待的应用程序暂停时间
- 通过-XX:MaxgcpauseMillis 参数来设置。这一点有点类似于 Parallel Scavenge收集器。关于停顿时间的设置并不是越短越好，设置的时间越短意味着每次收集的CSet越小,导致垃圾逐步积累变多,最终不得不退化成 Serial GC;停顿时间设置的过长那么会导致每次都会产生长时间的停顿影响了程序对外的响应时间

#### Mixed GC的触发时机

触发时机由一些参数控制,另外这些参数也控制着哪些老年代 Region会被选入CSet(收集集合)

- 参数：G1heapwastepercent: Global concurrent marking结束之后,我们可以知道 old gen regions中有多少空间要被回收在每次YGC之后和再次发生Mixed GC之前【YGC是在Eden空间满了就进行GC】会检査垃圾占比是否达到此参数,只有达到了,下次才会发生 Mixed GC
- 参数：G1MixedGCliveThresholdPercent: old generation region中的存活对象的占比,只有占比低于此参数,该old generation region才会被选入CSet
- 参数：G1MixedGCCountTarget:一次global concurrent marking之后,最多执行 MixedGC的次数
- 参数：G1OldCSetRegionThresholdPercent：一次Mxed GC中能被选入Cset的最多old generation region数量
- ![1583073184480](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164739-33717.png)



### G1的收集概览

G1算法将堆划分为若干个区域( Region),它仍然属于分代收集器。不过,这些区域的一部分包含新生代和老年代,新生代的垃圾收集依然采用暂停所有应用线程的方式,将存活对象拷贝到老年代或者Survivor空间。老年代也分成很多区域,G1收集器通过将对象从一个区域复制到另外一个区域,完成了清理工作。这就意味着,在正常的处理过程中,G1完成了堆的压缩(至少是部分堆的压缩),这样也就不会有CMS内存碎片问题的存在

**YGC收集时如何找到根对象呢？老年代所有的对象都是根对象吗？那如果直接对整个老年代进行扫描的话会耗费大量的时间。于是G1引入了我们上面提到的RSet的概念，作用是跟踪指向某个heap区内的对象引用。但是在新生代之间记录引用吗？这是不必要的，原因在于每次GC时所有新生代都会被扫描,所以只需要记录老年代到新生代之间的引用即可。YGC收集过程如下：，不懂的原因，对Rset 和point out point into 相关还是不理解**

![1583157404318](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164737-430638.png)





### 三色标记算法

这是Mixed gc 中的Global  concurrent marking中的内容，提到并发标记,我们不得不了解并发标记的三色标记算法，mark的过程就是遍历heap标记 live object的过程。它是描述追踪式回收器的一种有效的方法,利用它可以推演回收器的正确性。我们将对象分成三种类型:

- 黑色:根对象或者该对象与它的子对象都被扫描过(即对象被标记了,且它的所有feld也被标记完了)
- 灰色:对象本身被扫描,但还没扫描完该对象中的子对象(即它的 field还没有被标记或标记完)
- 白色:未被扫描对象,如果扫描完成所有对象之后最终为白色的为不可达对象,即垃圾对象(即对象没有被标记到）

#### 标记过程演示

- ![1583131636331](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164735-110381.png)
- ![1583131729874](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164734-939434.png)
- ![1583131855524](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164732-672809.png)

#### 可能出现的问题

- ![1583132245202](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164731-531778.png)

- 这时候应用程序执行了以下操作A.c=C和B.c=null这样,对象的状态图变成如下情形

- ![1583132314470](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164730-838216.png)

- 这时候垃圾收集器再去标记扫描的时候就会变成下图这样，这样就会造成漏标：

- ![1583132429116](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304164728-496925.png)

  

  

#### 解决方法-SATB

  SATB snapshot at the beginning

- 在开始的时候生成一个快照图，标记存活的对象
- 在并发标记的时候所有被改变的对象入队（在write barrier里把所有旧的引用所指向的对象都变成非白的，如上图中，将B所指向的引用C变成非白的）
- 可能存在浮动垃圾，将在下次垃圾收集时被收集





## SATB详解

- SATB详解SATB是维持并发GC的一种手段。G1并发的基础就是SATB。SATB可以理解成在GC开始之前对堆内存里的对象做一次快照，此时活的对象就认为是活的,从而形成个对象图。在GC收集的时候,**新生代的对象也认为是活的对象,除此之外其他不可达的对象都认为是垃圾对象**
- 如何找到在GC过程中分配的对象呢? 这是收集过程中可能遇到的第二个问题，每个Region记录着两个top-at-mark- start(TAMS)指针,分别为 prevtamst和 next AMS。在TAMS以上的对象就是新分配的,因而被视为隐式 marked。通过这种方式我们就找到了在GC过程中新分配的对象,并把这些对象认为是活的对象
- 解决了对象在GC过程中分配的问题,那么在GC过程中引用发生变化的问题怎么解决呢?Write Barrier就是对引用字段进行赋值做额外处理。通过 Write Barrier就可以了解到哪些引用对象发生了什么样的变化。



- **SATB仅仅对于在 marking开始阶段进行snapshot"(marked all reachable at markstart))**,但是 concurrent的时候并发修改可能造成对象漏标记
  - 对 black新引用了一个 white对象,然后又从gray对象中删除了对该 white对象的引用这样会造成了该whie对象漏标记
  - 对 black新引用了一个 white对象,然后从gray对象删了一个引用该 white对象的 white对象,这样也会造成了该 white对象漏标记
  - 对 black新引用了一个刚new出来的 white对象,没有其他gray对象引用该 White对象这样也会造成了该 white对象漏标记
  - 对于三色算法在 concurrent的时候可能产生的漏标记问题,SATB在 marking阶段中对于从gray对象移除的目标引用对象标记为gray,对于 black引用的新产生的对象标记为 black；**由于是在开始的时候进行snapshot,因而可能存在 Floating Garbage**
- 漏标与误标
  - 误标没什么关系,顶多造成浮动垃圾,在下次gc还是可以回收的,但是漏标的后果是致命的把本应该存活的对象给回收了,从而影响的程序的正确性
  - 漏标的情况只会发生在白色对象中,且满足以下任意一个条件
    - 并发标记时,应用线程给一个黑色对象的引用类型字段赋值了该白色对象
    - 并发标记时,应用线程删除所有灰色对象到该白色对象的引用（但是此时或许有黑色对象引用该白色对象）
    - 如何解决以上问题？
      - 对于第一种情况,利用 post-write barrier,记录所有新增的引用关系,然后根据这些引用关系为根重新扫描一遍，Write Barrier就是对引用字段进行赋值做额外处理。通过 Write Barrier就可以了解到哪些引用对象发生了什么样的变化
      - 对于第二种情况,利用pre-write barrier,将所有即将被删除的引用关系的旧引用记录下来,最后以这些旧引用为根重新扫描遍，这样就能扫描标记到那个“白色对象”





## G1最佳实践

### 不断调优暂停时间指标

通过 XX. MaxGcPauseMillis=x 可以设置启动应用程序暂停的时间,G1在运行的时候会根据这个参数选择Cset来满足响应时间的设置。一般情况下这个值设置到100ms或者200ms都是可以的(不同情况下会不一样),但如果设置成50ms就不太合理。暂停时间设置的太短,就会导致出现G1跟不上垃圾产生的速度。最终退化成 Full gc。所以对这个参数的调优是一个持续的过程,逐步调整到最佳状态。

### 不要设置新生代和老年代的大小

G1收集器在运行的时候会调整新生代和老年代的大小。通过改变代的大小来调整对象晋升的速度以及晋升年龄,从而达到我们为收集器设置的暂停时间目标。设置了新生代大小相当于放弃了G1为我们做的自动调优。我们需要做的只是设置整个堆内存的大小,剩下的交给G1自己去分配各个代的大小即可

### 关注 Evacuation Failure

Evacuation Failure【Evacuation是拷贝存活对象的意思，是GC步骤之一，GC步骤分为两步全局并发标记(global concurrent marking)和拷贝存活对象(evacuation)】 类似于CMS里面的晋升失败堆空间的垃圾太多导致无法完成 Region之间的拷贝,于是不得不退化成 Full GC来做一次全局范围内的垃圾收集



## 对比其它收集器

- G1 VS CMS对比使用mark- sweep的CMS,G1使用的copying算法不会造成内存碎片
- 对比 Parallel Scavenge(基于 copying)Parallel Old收集器(基于mark- compact-sweep), Parallel会对整个区域做整理导致gc停顿会比较长,而G1只是特定地整理压缩某些Region
  - G1并非一个实时的收集器,与 parallel Scavenge一样,对gc停顿时间的设置并不绝对生效,只是G1有较高的几率保证不超过设定的gc停顿时间。与之前的gc收集器对比,G1会根据用户设定的gc停顿时间智能评估哪几个 region需要被回收可以满足用户的设定



### G1实验

#### 实验代码

#### 实验参数

#### 实验结果

```properties
2020-03-03T11:06:20.055+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark), 0.0024128 secs]
   [Parallel Time: 1.3 ms, GC Workers: 8]
   # GC Workers: 8 ,当前的GC线程数为8
      [GC Worker Start (ms): Min: 178.9, Avg: 178.9, Max: 178.9, Diff: 0.1]
      # GC线程开始
      [Ext Root Scanning (ms): Min: 0.3, Avg: 0.4, Max: 0.5, Diff: 0.3, Sum: 3.2]
   	  # 根节点扫描
      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      # 更新RSet
         [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 0.6, Avg: 0.7, Max: 0.8, Diff: 0.2, Sum: 5.8]
      [Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      # 处理引用队列
         [Termination Attempts: Min: 1, Avg: 6.0, Max: 9, Diff: 8, Sum: 48]
    # 此以上的步骤都是和之前说到的YGC的处理流程是一一对应的
      [GC Worker Other (ms): Min: 0.0, Avg: 0.1, Max: 0.3, Diff: 0.3, Sum: 0.9]
      [GC Worker Total (ms): Min: 1.2, Avg: 1.2, Max: 1.3, Diff: 0.1, Sum: 10.0]
      [GC Worker End (ms): Min: 180.2, Avg: 180.2, Max: 180.2, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.1 ms]
   # 即clear CartTable 
   [Other: 1.0 ms]
      [Choose CSet: 0.0 ms]
      # 选择要回收的Region
      [Ref Proc: 0.1 ms]
      [Ref Enq: 0.0 ms]
      # 引用的处理
      [Redirty Cards: 0.1 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 2048.0K(6144.0K)->0.0B(2048.0K) Survivors: 0.0B->1024.0K Heap: 3990.1K(10.0M)->2847.6K(10.0M)]
   # 表示执行完YGC之后，整个eden空间的结果，可以看到执行完之后eden空间变成了0；Survivor空间变成1m，这是从新生代来的
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-03-03T11:06:20.058+0800: [GC concurrent-root-region-scan-start]
2020-03-03T11:06:20.059+0800: [GC pause (G1 Humongous Allocation) (young)2020-03-03T11:06:20.059+0800: [GC concurrent-root-region-scan-end, 0.0011467 secs]
2020-03-03T11:06:20.059+0800: [GC concurrent-mark-start]
, 0.0019177 secs]
# 以上是并发的一些处理
   [Root Region Scan Waiting: 0.5 ms]
   [Parallel Time: 0.9 ms, GC Workers: 8]
      [GC Worker Start (ms): Min: 182.5, Avg: 182.6, Max: 182.8, Diff: 0.3]
      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.2, Max: 0.3, Diff: 0.3, Sum: 1.2]
      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 0.4, Avg: 0.4, Max: 0.5, Diff: 0.1, Sum: 3.5]
      [Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.4]
         [Termination Attempts: Min: 1, Avg: 4.8, Max: 8, Diff: 7, Sum: 38]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [GC Worker Total (ms): Min: 0.4, Avg: 0.7, Max: 0.9, Diff: 0.4, Sum: 5.2]
      [GC Worker End (ms): Min: 183.3, Avg: 183.3, Max: 183.4, Diff: 0.1]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.1 ms]
   [Other: 0.4 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.2 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.1 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 1024.0K(2048.0K)->0.0B(1024.0K) Survivors: 1024.0K->1024.0K Heap: 3912.6K(10.0M)->3895.4K(10.0M)]
   # 第二次YGC的结果
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-03-03T11:06:20.061+0800: [GC concurrent-mark-end, 0.0015466 secs]
2020-03-03T11:06:20.061+0800: [Full GC (Allocation Failure)  3895K->3731K(10M), 0.0042062 secs]
   [Eden: 0.0B(1024.0K)->0.0B(1024.0K) Survivors: 1024.0K->0.0B Heap: 3895.4K(10.0M)->3731.8K(10.0M)], [Metaspace: 3113K->3113K(1056768K)]
 [Times: user=0.03 sys=0.00, real=0.00 secs] 
2020-03-03T11:06:20.066+0800: [GC remark, 0.0000313 secs]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-03-03T11:06:20.066+0800: [GC concurrent-mark-abort]
完成了
Heap
 garbage-first heap   total 10240K, used 4755K [0x00000000ff600000, 0x00000000ff700050, 0x0000000100000000)
  region size 1024K, 1 young (1024K), 0 survivors (0K)
  # 堆的空间分配情况
 Metaspace       used 3238K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 352K, capacity 388K, committed 512K, reserved 1048576K

Process finished with exit code 0

```

