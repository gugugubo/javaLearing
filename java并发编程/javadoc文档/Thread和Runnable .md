

#### Thread类的javadoc文档

```
When a Java Virtual Machine starts up, there is usually a single non-daemon thread (which typically calls the method named main of some designated class). The Java Virtual Machine continues to execute threads until either of the following occurs:
当Java虚拟机启动时，通常只有一个非守护进程线程（它通常调用某些指定类中名为main的方法）。Java虚拟机继续执行线程，直到发生以下任一情况：
The exit method of class Runtime has been called and the security manager has permitted the exit operation to take place.
类运行时的exit方法已被调用，安全管理器已允许执行exit操作。
All threads that are not daemon threads have died, either by returning from the call to the run method or by throwing an exception that propagates beyond the run method.
不是守护进程线程的所有线程都已死亡，方法是从对run方法的调用返回，或者引发传播到run方法之外的异常。

There are two ways to create a new thread of execution. One is to declare a class to be a subclass of Thread. This subclass should override the run method of class Thread. An instance of the subclass can then be allocated and started. For example, a thread that computes primes larger than a stated value could be written as follows:
有两种方法可以创建新的执行线程。一种是将类声明为Thread的子类。这个子类应该重写类线程的run方法。然后可以分配并启动子类的实例。例如，计算大于指定值的素数的线程可以如下编写：

class PrimeThread extends Thread {
           long minPrime;
           PrimeThread(long minPrime) {
               this.minPrime = minPrime;
           }
  
           public void run() {
               // compute primes larger than minPrime
                . . .
           }
       }
下面的代码将创建一个线程并启动它运行：
     PrimeRun p = new PrimeRun(143);
       new Thread(p).start();
       
The other way to create a thread is to declare a class that implements the Runnable interface. That class then implements the run method. An instance of the class can then be allocated, passed as an argument when creating Thread, and started. The same example in this other style looks like the following:
创建线程的另一种方法是声明实现Runnable接口的类。然后，该类实现run方法。然后可以分配类的实例，在创建Thread类时作为参数传递，然后启动。相同示例如下所示：

       class PrimeRun implements Runnable {
           long minPrime;
           PrimeRun(long minPrime) {
               this.minPrime = minPrime;
           }
           public void run() {
               // compute primes larger than minPrime
                . . .
           }
       }
启动
       PrimeRun p = new PrimeRun(143);
       new Thread(p).start();

Every thread has a name for identification purposes. More than one thread may have the same name. If a name is not specified when a thread is created, a new name is generated for it.
每个线程都有一个用于标识的名称。多个线程可能具有相同的名称。如果在创建线程时未指定名称，则会为其生成新名称。
```



##### Thread 的start() 方法

```
Causes this thread to begin execution; the Java Virtual Machine calls the run method of this thread.
使此线程开始执行；Java虚拟机调用此线程的run方法。 

The result is that two threads are running concurrently: the current thread  (which returns from the call to the start method) and the other thread  (which executes its run method).
结果是两个线程同时运行：当前线程和另一个线程（执行其run方法）。 
```



#####   Thread 的run()方法的javadoc文档

```
 1.如果此线程是使用单独的Runnable run对象构造的，则调用该Runnable对象的run方法；否则，此方法不执行任何操作并返回。 
 2.Subclasses of Thread should override this method. 线程的子类应重写此方法。 
```







#### Runnable 类得javadoc文档

```


The Runnable interface should be implemented by any class whose instances are intended to be executed by a thread. The class must define a method of no arguments called run.
实例要由线程执行的类要实现Runnable接口。类必须定义一个名为run的无参数方法。

This interface is designed to provide a common protocol for objects that wish to execute code while they are active. For example, Runnable is implemented by class Thread. Being active simply means that a thread has been started and has not yet been stopped.
此接口旨在为希望在活动时执行代码的对象提供通用协议。例如，Runnable被Thread类实现。处于活动状态只意味着线程已启动但尚未停止。

In addition, Runnable provides the means for a class to be active while not subclassing Thread. A class that implements Runnable can run without subclassing Thread by instantiating a Thread instance and passing itself in as the target. In most cases, the Runnable interface should be used if you are only planning to override the run() method and no other Thread methods. This is important because classes should not be subclassed unless the programmer intends on modifying or enhancing the fundamental behavior of the class.
此外，Runnable提供了一种方法，使类在不继承Thread类的情况下处于活动状态。实现Runnable的类可以通过实例化一个Thread实例并将其自身作为目标传入来运行，而无需继承Thread类。在大多数情况下，如果只计划重写run()方法，而不打算重写其他线程方法，则应使用Runnable接口。这一点很重要，因为除非程序员打算修改或增强类的基本行为，否则不应该对类进行子类化。 

This method should only be called by a thread that is the owner of this object's monitor. See the notify method for a description of the ways in which a thread can become the owner of a monitor.
此方法只能由作为此对象监视器所有者的线程调用。有关线程成为监视器所有者的方式的描述，请参见notify方法。
```

