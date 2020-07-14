

# 5. 共享模型之内存

上一章讲解的 Monitor 主要关注的是访问共享变量时，保证临界区代码的原子性。这一章我们进一步深入学习共享变量在多线程间的【可见性】问题与多条指令执行时的【有序性】问题

## 5.1 Java 内存模型

JMM 即 Java Memory Model，它从java层面定义了主存、工作内存抽象概念，底层对应着 CPU 寄存器、缓存、硬件内存、
CPU 指令优化等。JMM 体现在以下几个方面

1. 原子性 - 保证指令不会受到线程上下文切换的影响
2. 可见性 - 保证指令不会受 cpu 缓存的影响
3. 有序性 - 保证指令不会受 cpu 指令并行优化的影响



## 5.2 可见性

### 退不出的循环

先来看一个现象，main 线程对 run 变量的修改对于 t 线程不可见，导致了 t 线程无法停止：Test1.java

```java
public class Test1 {
    static boolean run = true;
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            while(run){
                // ....
//                System.out.println(2323);  如果加上这个代码就会停下来
            }
        });
        t.start();
        utils.sleep(1);
        System.out.println(3434);   
        run = false; // 线程t不会如预想的停下来
    }
}
```



为什么呢？分析一下：

1. 初始状态， t 线程刚开始从主内存读取了 run 的值到工作内存。
   1. ![1594646434877](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200713222445-555075.png)
2. 因为t1线程频繁地从主存中读取run的值，jit即时编译器会将run的值缓存至自己工作内存中的高速缓存中，减少对主存中run的访问以提高效率
   1. ![1594646562777](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200713212457-709749.png)
3.  1 秒之后，main 线程修改了 run 的值，并同步至主存，而 t 是从自己工作内存中的高速缓存中读取这个变量
   的值，结果永远是旧值
   1. ![1594646581590](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200713212306-584613.png)



### 解决方法

volatile（表示易变关键字的意思）
它可以用来修饰成员变量和静态成员变量，他可以避免线程从自己的工作缓存中查找变量的值，必须到主存中获取
它的值，线程操作 volatile 变量都是直接操作主存   Test1.java   

使用synchronized关键字也有相同的效果！在Java内存模型中，synchronized规定，线程在加锁时， 先清空工作内存→在主内存中拷贝最新变量的副本到工作内存 →执行完代码→将更改后的共享变量的值刷新到主内存中→释放互斥锁。Test2.java

### 可见性 vs 原子性

前面

例子体现的实际就是可见性，它保证的是在多个线程之间一个线程对 volatile 变量的修改对另一个线程可
见， 而不能保证原子性，仅用在一个写线程，多个读线程的情况： 上例从字节码理解是这样的：

```
getstatic run // 线程 t 获取 run true
getstatic run // 线程 t 获取 run true
getstatic run // 线程 t 获取 run true
getstatic run // 线程 t 获取 run true
putstatic run // 线程 main 修改 run 为 false， 仅此一次
getstatic run // 线程 t 获取 run false 
```

比较一下之前我们将线程安全时举的例子：两个线程一个 i++ 一个 i-- ，只能保证看到最新值，不能解决指令交错

```
// 假设i的初始值为0
getstatic i // 线程2-获取静态变量i的值 线程内i=0
getstatic i // 线程1-获取静态变量i的值 线程内i=0
iconst_1 // 线程1-准备常量1
iadd // 线程1-自增 线程内i=1
putstatic i // 线程1-将修改后的值存入静态变量i 静态变量i=1
iconst_1 // 线程2-准备常量1
isub // 线程2-自减 线程内i=-1
putstatic i // 线程2-将修改后的值存入静态变量i 静态变量i=-1 
```

> 注意 synchronized 语句块既可以保证代码块的原子性，也同时保证代码块内变量的可见性。但缺点是
> synchronized 是属于重量级操作，性能相对更低
> 如果在前面示例的死循环中加入 System.out.println() 会发现即使不加 volatile 修饰符，线程 t 也能正确看到
> 对 run 变量的修改了，想一想为什么？因为println方法里面有synchronized修饰。还有那个等烟的示例(Test34.java)为啥没有出现可见性问题?和synchrozized是一个道理。



### 模式之两阶段终止

使用volatile关键字来实现两阶段终止模式，上代码：Test3.java

### 模式之 Balking 

1. 定义
Balking （犹豫）模式用在一个线程发现另一个线程或本线程已经做了某一件相同的事，那么本线程就无需再做
了，直接结束返回。有点类似与单例。 
2. 实现  Test4.java





## 5.3 有序性

### 诡异的结果

```java
int num = 0;

// volatile 修饰的变量，可以禁用指令重排 volatile boolean ready = false; 可以防止变量之前的代码被重排序
boolean ready = false; 
// 线程1 执行此方法
public void actor1(I_Result r) {
 if(ready) {
 	r.r1 = num + num;
 } 
 else {
 	r.r1 = 1;
 }
}
// 线程2 执行此方法
public void actor2(I_Result r) {
 num = 2;
 ready = true;
}

```

分别执行上面两个线程

I_Result 是一个对象，有一个属性 r1 用来保存结果，问可能的结果有几种？
有同学这么分析
情况1：线程1 先执行，这时 ready = false，所以进入 else 分支结果为 1
情况2：线程2 先执行 num = 2，但没来得及执行 ready = true，线程1 执行，还是进入 else 分支，结果为1
情况3：线程2 执行到 ready = true，线程1 执行，这回进入 if 分支，结果为 4（因为 num 已经执行过了）

但我告诉你，结果还有可能是 0 ，信不信吧！这种情况下是：线程2 执行 ready = true，切换到线程1，进入 if 分支，相加为 0，再切回线程2 执行 num = 2

这种现象叫做指令重排，是 JIT 编译器在运行时的一些优化，这个现象需要通过大量测试才能复现，可以使用jcstress工具进行测试。上面仅是从代码层面体现出了有序性问题，下面在讲到 double-checked locking 问题时还会从java字节码的层面了解有序性的问题。

重排序也需要遵守一定规则：

1. 重排序操作不会对存在数据依赖关系的操作进行重排序。比如：a=1;b=a; 这个指令序列，由于第二个操作依赖于第一个操作，所以在编译时和处理器运行时这两个操作不会被重排序。
2. 重排序是为了优化性能，但是不管怎么重排序，单线程下程序的执行结果不能被改变。比如：a=1;b=2;c=a+b这三个操作，第一步（a=1)和第二步(b=2)由于不存在数据依赖关系，所以可能会发生重排序，但是c=a+b这个操作是不会被重排序的，因为需要保证最终的结果一定是c=a+b=3。

重排序在单线程模式下是一定会保证最终结果的正确性，但是在多线程环境下，问题就出来了。解决方法：volatile 修饰的变量，可以禁用指令重排

> 注意：使用synchronized并不能解决有序性问题，但是如果是该变量整个都在synchronized代码块的保护范围内，那么变量就不会被多个线程同时操作，也不用考虑有序性问题！在这种情况下相当于解决了重排序问题！参考double-checked locking 问题里的代码，第一个代码片段中的instance变量都在synchronized代码块中，第二个代码片段中instance不全在synchronized中所以产生了问题。   视频 P151



### volatile 原理

volatile 的底层实现原理是内存屏障，Memory Barrier（Memory Fence）

1. 对 volatile 变量的写指令后会加入写屏障
2. 对 volatile 变量的读指令前会加入读屏障

#### 如何保证可见性

1. 写屏障（sfence）保证在该屏障之前的，对共享变量的改动，都同步到主存当中

  ```java
  public void actor2(I_Result r) {
   num = 2;
   ready = true; // ready 是 volatile 赋值带写屏障
   // 写屏障
  }
  
  ```

2. 而读屏障（lfence）保证在该屏障之后，对共享变量的读取，加载的是主存中最新数据

   ```java
   public void actor1(I_Result r) {
    // 读屏障
    // ready 是 volatile 读取值带读屏障
    if(ready) {
    r.r1 = num + num;
    } else {
    r.r1 = 1;
    }
   }
   ```

![1594698374315](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200714114615-934315.png)



#### 如何保证有序性

1. 写屏障会确保指令重排序时，不会将写屏障之前的代码排在写屏障之后

   ```
   public void actor2(I_Result r) {
    num = 2;
    ready = true; // ready 是 volatile 赋值带写屏障
    // 写屏障
   }
   ```

2. 读屏障会确保指令重排序时，不会将读屏障之后的代码排在读屏障之前

   ```java
   public void actor1(I_Result r) {
    // 读屏障
    // ready 是 volatile 读取值带读屏障
    if(ready) {
    	r.r1 = num + num;
    } else {
    	r.r1 = 1;
    }
   }
   ```

   ![1594698559052](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200714114921-56542.png)



还是那句话，不能解决指令交错：

1. 写屏障仅仅是保证之后的读能够读到最新的结果，但不能保证读跑到它前面去
2. 而有序性的保证也只是保证了本线程内相关代码不被重排序

![1594698671628](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200714115112-322421.png)



####  double-checked locking 问题

以著名的 double-checked locking 单例模式为例，这是volatile最常使用的地方。

```
//最开始的单例模式是这样的
    public final class Singleton {
        private Singleton() { }
        private static Singleton INSTANCE = null;
        public static Singleton getInstance() {
        // 首次访问会同步，而之后的使用不用进入synchronized
        synchronized(Singleton.class) {
        	if (INSTANCE == null) { // t1
        		INSTANCE = new Singleton();
            }
        }
            return INSTANCE;
        }
    }
//但是上面的代码块的效率是有问题的，因为即使已经产生了单实例之后，之后调用了getInstance()方法之后还是会加锁，这会严重影响性能！因此就有了模式如下double-checked lockin：
    public final class Singleton {
        private Singleton() { }
        private static Singleton INSTANCE = null;
        public static Singleton getInstance() {
            if(INSTANCE == null) { // t2
                // 首次访问会同步，而之后的使用没有 synchronized
                synchronized(Singleton.class) {
                    if (INSTANCE == null) { // t1
                        INSTANCE = new Singleton();
                    }
                }
            }
            return INSTANCE;
        }
    }
//但是上面的if(INSTANCE == null)判断代码没有在同步代码块synchronized中，不能享有synchronized保证的原子性，可见性。所以
```

以上的实现特点是：

1. 懒惰实例化
2. 首次使用 getInstance() 才使用 synchronized 加锁，后续使用时无需加锁
3. 有隐含的，但很关键的一点：第一个 if 使用了 INSTANCE 变量，是在同步块之外

但在多线程环境下，上面的代码是有问题的，getInstance 方法对应的字节码为：

```
0: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
3: ifnonnull 37
// ldc是获得类对象
6: ldc #3 // class cn/itcast/n5/Singleton
// 将类对象的引用地址复制了一份
8: dup
// 将类对象的引用地址存储了一份，是为了将来解锁用
9: astore_0
10: monitorenter
11: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
14: ifnonnull 27
// 新建一个实例
17: new #3 // class cn/itcast/n5/Singleton
// 复制了一个实例的引用
20: dup
// 通过这个复制的引用调用它的构造方法
21: invokespecial #4 // Method "<init>":()V
// 最开始的这个引用用来进行赋值操作
24: putstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
27: aload_0
28: monitorexit
29: goto 37
32: astore_1
33: aload_0
34: monitorexit
35: aload_1
36: athrow
37: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
40: areturn

```

其中

1. 17 表示创建对象，将对象引用入栈 // new Singleton
2. 20 表示复制一份对象引用 // 复制了引用地址
3. 21 表示利用一个对象引用，调用构造方法  // 根据复制的引用地址调用构造方法
4. 24 表示利用一个对象引用，赋值给 static INSTANCE

也许 jvm 会优化为：先执行 24，再执行 21。如果两个线程 t1，t2 按如下时间序列执行：

![1594701748458](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200714124230-412629.png)

关键在于 `0: getstatic` 这行代码在 monitor 控制之外，它就像之前举例中不守规则的人，可以越过 monitor 读取
INSTANCE 变量的值
这时 t1 还未完全将构造方法执行完毕，如果在构造方法中要执行很多初始化操作，那么 t2 拿到的是将是一个未初
始化完毕的单例
对 INSTANCE 使用 volatile 修饰即可，可以禁用指令重排，但要注意在 JDK 5 以上的版本的 volatile 才会真正有效

#### double-checked locking 解决

加volatile就行了

```java
    public final class Singleton {
        private Singleton() { }
        private static volatile Singleton INSTANCE = null;
        public static Singleton getInstance() {
            // 实例没创建，才会进入内部的 synchronized代码块
            if (INSTANCE == null) {
                synchronized (Singleton.class) { // t2
                    // 也许有其它线程已经创建实例，所以再判断一次
                    if (INSTANCE == null) { // t1
                        INSTANCE = new Singleton();
                    }
                }
            }
            return INSTANCE;
        }
    }

```

字节码上看不出来 volatile 指令的效果

```
// -------------------------------------> 加入对 INSTANCE 变量的读屏障
0: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
3: ifnonnull 37
6: ldc #3 // class cn/itcast/n5/Singleton
8: dup
9: astore_0
10: monitorenter -----------------------> 保证原子性、可见性
11: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
14: ifnonnull 27
17: new #3 // class cn/itcast/n5/Singleton
20: dup
21: invokespecial #4 // Method "<init>":()V
24: putstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
// -------------------------------------> 加入对 INSTANCE 变量的写屏障
27: aload_0
28: monitorexit ------------------------> 保证原子性、可见性
29: goto 37
32: astore_1
33: aload_0
34: monitorexit
35: aload_1
36: athrow
37: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
40: areturn
```



如上面的注释内容所示，读写 volatile 变量操作（即getstatic操作和putstatic操作）时会加入内存屏障（Memory Barrier（Memory Fence）），保证下面两点：

1. 可见性
   1. 写屏障（sfence）保证在该屏障之前的 t1 对共享变量的改动，都同步到主存当中
   2. 而读屏障（lfence）保证在该屏障之后 t2 对共享变量的读取，加载的是主存中最新数据
2. 有序性
   1. 写屏障会确保指令重排序时，不会将写屏障之前的代码排在写屏障之后
   2. 读屏障会确保指令重排序时，不会将读屏障之后的代码排在读屏障之前
3. 更底层是读写变量时使用 lock 指令来多核 CPU 之间的可见性与有序性

![1594703228878](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200714130925-543886.png)





### happens-before

下面说的变量都是指成员变量或静态成员变量

1. 线程解锁 m 之前对变量的写，对于接下来对 m 加锁的其它线程对该变量的读可见

   1. ```java
              static int x;
              static Object m = new Object();
              new Thread(()->{
                  synchronized(m) {
                      x = 10;
                  }
              },"t1").start();
              new Thread(()->{
                  synchronized(m) {
                      System.out.println(x);
                  }
              },"t2").start();
               
      ```

2. 线程对 volatile 变量的写，对接下来其它线程对该变量的读可见

   1.     ```java
          volatile static int x;
          new Thread(()->{
           x = 10;
          },"t1").start();
          new Thread(()->{
           System.out.println(x);
          },"t2").start();
          ```
          
   
3. 线程 start 前对变量的写，对该线程开始后对该变量的读可见

   1.     ```java
          static int x;
          x = 10;
          new Thread(()->{
           System.out.println(x);
          },"t2").start();
          ```


4. 线程结束前对变量的写，对其它线程得知它结束后的读可见（比如其它线程调用 t1.isAlive() 或 t1.join()等待它结束）

   1.   
        
          ```
          static int x;
          Thread t1 = new Thread(()->{
           x = 10;
          },"t1");
          t1.start();
          t1.join();
          System.out.println(x);
          ```
        
        ​     
   
5.  线程 t1 打断 t2（interrupt）前对变量的写，对于其他线程得知 t2 被打断后对变量的读可见（通过
   t2.interrupted 或 t2.isInterrupted）

   1. ```
          
                  static int x;
                  public static void main(String[] args) {
                      Thread t2 = new Thread(()->{
                          while(true) {
                              if(Thread.currentThread().isInterrupted()) {
                                  System.out.println(x);
                                  break;
                              }
                          }
                      },"t2");
                      t2.start();
                      new Thread(()->{
                          sleep(1);
                          x = 10;
                          t2.interrupt();
                      },"t1").start();
                      while(!t2.isInterrupted()) {
                          Thread.yield();
                      }
                      System.out.println(x);
                  }
                 
          ```
          
          

6. 对变量默认值（0，false，null）的写，对其它线程对该变量的读可见

7. 具有传递性，如果 x hb-> y 并且 y hb-> z 那么有 x hb-> z ，配合 volatile 的防指令重排，有下面的例子

   1. ​     
      ​    
          ```
            volatile static int x;
                  static int y;
                  new Thread(()->{
                      y = 10;
                      x = 20;
                  },"t1").start();
                  new Thread(()->{
                      // x=20 对 t2 可见, 同时 y=10 也对 t2 可见
                      System.out.println(x);
                  },"t2").start();
          ```
      
      
   ​    
   

   
   

### 总结

volatile主要用在一个线程改多个线程读时的来保证可见性，和double-checked locking模式中保证synchronized代码块外的共享变量的重排序问题



### 习题

#### balking 模式习题

希望 doInit() 方法仅被调用一次，下面的实现是否有问题，为什么？



```java
public class TestVolatile {
    volatile boolean initialized = false;
    void init() {
        if (initialized) {
            return;
        }
        doInit();
        initialized = true;
    }
    private void doInit() {
    }
} 
```

#### 线程安全单例习题

单例模式有很多实现方法，饿汉、懒汉、静态内部类、枚举类，试分析每种实现下获取单例对象（即调用
getInstance）时的线程安全，并思考注释中的问题
饿汉式：类加载就会导致该单实例对象被创建
懒汉式：类加载不会导致该单实例对象被创建，而是首次使用该对象时才会创建

实现1：

```java
// 问题1：为什么加 final，防止子类继承后更改
// 问题2：如果实现了序列化接口, 还要做什么来防止反序列化破坏单例，如果进行反序列化的时候会生成新的对象，这样跟单例模式生成的对象是不同的。要解决直接加上readResolve()方法就行了，如下所示
public final class Singleton implements Serializable {
    // 问题3：为什么设置为私有? 放弃其它类中使用new生成新的实例，是否能防止反射创建新的实例?不能。
    private Singleton() {}
    // 问题4：这样初始化是否能保证单例对象创建时的线程安全?没有，这是类变量，是jvm在类加载阶段就进行了初始化，jvm保证了此操作的线程安全性
    private static final Singleton INSTANCE = new Singleton();
    // 问题5：为什么提供静态方法而不是直接将 INSTANCE 设置为 public, 说出你知道的理由。
    //1.提供更好的封装性；2.提供范型的支持
    public static Singleton getInstance() {
        return INSTANCE;
    }
    public Object readResolve() {
        return INSTANCE;
    }
}
```

实现2：

```java
// 问题1：枚举单例是如何限制实例个数的：创建枚举类的时候就已经定义好了，每个枚举常量其实就是枚举类的一个静态成员变量
// 问题2：枚举单例在创建时是否有并发问题：没有，这是静态成员变量
// 问题3：枚举单例能否被反射破坏单例：不能
// 问题4：枚举单例能否被反序列化破坏单例：枚举类默认实现了序列化接口，枚举类已经考虑到此问题，无需担心破坏单例
// 问题5：枚举单例属于懒汉式还是饿汉式：饿汉式
// 问题6：枚举单例如果希望加入一些单例创建时的初始化逻辑该如何做：加构造方法就行了
enum Singleton {
 INSTANCE;
}
```

实现3：

```java
public final class Singleton {
    private Singleton() { }
    private static Singleton INSTANCE = null;
    // 分析这里的线程安全, 并说明有什么缺点：synchronized加载静态方法上，可以保证线程安全。缺点就是锁的范围过大
    public static synchronized Singleton getInstance() {
        if( INSTANCE != null ){
            return INSTANCE;
        }
        INSTANCE = new Singleton();
        return INSTANCE;
    }
}

```



实现4：DCL

```java
public final class Singleton {
    private Singleton() { }
    // 问题1：解释为什么要加 volatile ?为了防止重排序问题
    private static volatile Singleton INSTANCE = null;

    // 问题2：对比实现3, 说出这样做的意义：提高了效率
    public static Singleton getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (Singleton.class) {
            // 问题3：为什么还要在这里加为空判断, 之前不是判断过了吗？这是为了第一次判断时的并发问题。
            if (INSTANCE != null) { // t2
                return INSTANCE;
            }
            INSTANCE = new Singleton();
            return INSTANCE;
        }
    }
}
```

实现5：

```
public final class Singleton {
    private Singleton() { }
    // 问题1：属于懒汉式还是饿汉式：懒汉式，这是一个静态内部类。类加载本身就是懒惰的，在没有调用getInstance方法时是没有执行LazyHolder内部类的类加载操作的。
    private static class LazyHolder {
        static final Singleton INSTANCE = new Singleton();
    }
    // 问题2：在创建时是否有并发问题，这是线程安全的，类加载时，jvm保证类加载操作的线程安全
    public static Singleton getInstance() {
        return LazyHolder.INSTANCE;
    }
}
```

## 5.4本章小结

本章重点讲解了 JMM 中的

1. 可见性 - 由 JVM 缓存优化引起
2. 有序性 - 由 JVM 指令重排序优化引起
3. happens-before 规则
4. 原理方面
   1. volatile
5. 模式方面
   1. 两阶段终止模式的 volatile 改进
   2. 同步模式之 balking





# 6. 共享模型之无锁

管程即monitor是阻塞式的悲观锁实现并发控制，这章我们将通过非阻塞式的乐观锁的来实现并发控制

