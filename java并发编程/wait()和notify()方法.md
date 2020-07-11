



### wait()方法的javadoc文档

```

Causes the current thread to wait until another thread invokes the notify() method or the notifyAll() method for this object. In other words, this method behaves exactly as if it simply performs the call wait(0).
使当前线程等待，直到另一个线程为此对象调用notify()方法或notifyAll()方法。换句话说，这个方法的行为就像它只是执行调用等待（0）一样。

The current thread must own this object's monitor. The thread releases ownership of this monitor and waits until another thread notifies threads waiting on this object's monitor to wake up either through a call to the notify method or the notifyAll method. The thread then waits until it can re-obtain ownership of the monitor and resumes execution.
当前线程必须拥有此对象的监视器(锁) MyTest1.java。当前线程释放此监视器的所有权(即释放锁)并等待，直到另一个线程通过调用notify方法或notifyAll方法通知等待此对象监视器唤醒的线程。然后线程等待，直到它可以重新获得监视器的所有权并继续执行。

As in the one argument version, interrupts and spurious wakeups are possible, and this method should always be used in a loop:
在单参数版本中，中断和虚假唤醒是可能的，此方法应始终在循环中使用： 
           synchronized (obj) {
               while (<condition does not hold>)
                   obj.wait();
               ... // Perform action appropriate to condition
           }

This method should only be called by a thread that is the owner of this object's monitor. See the notify method for a description of the ways in which a thread can become the owner of a monitor.
此方法只能由作为此对象监视器所有者的线程调用。有关线程成为监视器所有者的方式的描述，请参见notify方法。
```





### wait(long timeout)方法

```
The current thread must own this object's monitor.
当前线程必须拥有此对象的监视器。

This method causes the current thread (call it T) to place itself in the wait set for this object and then to relinquish any and all synchronization claims on this object. Thread T becomes disabled for thread scheduling purposes and lies dormant until one of four things happens:
此方法会导致当前线程（称为T）将自身放置在此对象的等待集中，然后放弃对此对象的任何和所有同步声明。出于线程调度的目的，线程T将被禁用，并处于休眠状态，直到发生以下四种情况之一：
1.Some other thread invokes the notify method for this object and thread T happens to be arbitrarily chosen as the thread to be awakened.
其他一些线程为此对象调用notify方法，而线程T恰好被任意选择为要唤醒的线程。
2.Some other thread invokes the notifyAll method for this object.
其他一些线程为此对象调用notifyAll方法。
3.Some other thread interrupts thread T.
另一个线程中断线程T。
4.The specified amount of real time has elapsed, more or less. If timeout is zero, however, then real time is not taken into consideration and the thread simply waits until notified.
指定的实时时间或多或少已经过去。但是，如果超时为零，则不考虑真实的时间，线程只需等待通知。

A thread can also wake up without being notified, interrupted, or timing out, a so-called spurious wakeup. While this will rarely occur in practice, applications must guard against it by testing for the condition that should have caused the thread to be awakened, and continuing to wait if the condition is not satisfied. In other words, waits should always occur in loops, like this one
线程也可以在不被通知、中断或超时的情况下唤醒，即所谓的虚假唤醒。虽然这种情况在实践中很少发生，但应用程序必须通过测试本应导致线程被唤醒的条件，并在条件不满足时也要继续等待来防范这种情况。换句话说，等待应该总是以循环的形式出现，就像下面这样  MyObject.java 中修改后带while的代码
           synchronized (obj) {
               while (<condition does not hold>)
                   obj.wait(timeout);
               ... // Perform action appropriate to condition
           }

If the current thread is interrupted by any thread before or while it is waiting, then an InterruptedException is thrown. This exception is not thrown until the lock status of this object has been restored as described above.
如果当前线程在等待之前或等待期间被任何线程interrupted，则抛出InterruptedException。在还原此对象的锁定状态（如上所述）之前，不会引发此异常。MyTest2.java

Note that the wait method, as it places the current thread into the wait set for this object, unlocks only this object; any other objects on which the current thread may be synchronized remain locked while the thread waits.
请注意，wait方法在将当前线程放入此对象的等待集中时，仅解锁此对象；在线程等待期间，可以同步当前线程的任何其他对象将保持锁定状态(可以在一个线程上对多个对象执行obj.wait())。
```



### notify方法

```
Wakes up a single thread that is waiting on this object's monitor. If any threads are waiting on this object, one of them is chosen to be awakened. The choice is arbitrary and occurs at the discretion of the implementation. A thread waits on an object's monitor by calling one of the wait methods.
唤醒等待此对象监视器的单个线程。如果有任何线程正在等待这个对象，则选择其中一个线程被唤醒。选择是任意的，由实现者自行决定。线程通过调用其中一个wait方法来等待对象的监视器。

The awakened thread will not be able to proceed until the current thread relinquishes the lock on this object. The awakened thread will compete in the usual manner with any other threads that might be actively competing to synchronize on this object; for example, the awakened thread enjoys no reliable privilege or disadvantage in being the next thread to lock this object.
在当前线程放弃对该对象的锁定之前，唤醒的线程将无法继续。唤醒的线程将以通常的方式与任何其他线程竞争，这些线程可能正在积极竞争以在此对象上同步；例如，唤醒的线程在成为下一个锁定此对象的线程时没有可靠的特权或劣势。

This method should only be called by a thread that is the owner of this object's monitor. A thread becomes the owner of the object's monitor in one of three ways:
此方法只能由作为此对象监视器所有者的线程调用。线程通过以下三种方式之一成为对象监视器的所有者：
1.By executing a synchronized instance method of that object.
通过执行该对象的synchronized实例方法。
2.By executing the body of a synchronized statement that synchronizes on the object.
通过执行同步对象的synchronized语句块。
3.For objects of type Class, by executing a synchronized static method of that class.
对于Class类型的对象，通过执行该类的synchronized静态方法。

Only one thread at a time can own an object's monitor.
一次只能有一个线程拥有对象的监视器。
```

### notifyAll方法

```
Wakes up all threads that are waiting on this object's monitor. A thread waits on an object's monitor by calling one of the wait methods.
唤醒此对象监视器上等待的所有线程。线程通过调用其中一个wait方法来等待对象的监视器。

The awakened threads will not be able to proceed until the current thread relinquishes the lock on this object. The awakened threads will compete in the usual manner with any other threads that might be actively competing to synchronize on this object; for example, the awakened threads enjoy no reliable privilege or disadvantage in being the next thread to lock this object.
在当前线程放弃对该对象的锁定之前，唤醒的线程将无法继续。唤醒的线程将以通常的方式与任何其他线程竞争，这些线程可能正在积极地竞争此对象上的synchronize；例如，唤醒的线程在成为锁定此对象的下一个线程时没有可靠的特权或劣势。

This method should only be called by a thread that is the owner of this object's monitor. See the notify method for a description of the ways in which a thread can become the owner of a monitor.
此方法只能由作为此对象监视器所有者的线程调用。有关线程成为监视器所有者的方式的描述，请参见notify方法。
```

### 总结

1. 当调用wait时，首先需要确保调用了wait方法的线程已经持有了对象的锁(调用wait方法的代码片段需要放在sychronized块或者时sychronized方法中，这样才可以确保线程在调用wait方法前已经获取到了对象的锁)
2. 当调用wait时，该线程就会释放掉这个对象的锁，然后进入等待状态(wait set)
3. 当线程调用了wait后进入到等待状态时，它就可以等待其他线程调用相同对象的notify或者notifyAll方法使得自己被唤醒
4. 一旦这个线程被其它线程唤醒之后，该线程就会与其它线程以同开始竞争这个对象的锁(公平竞争)；只有当该线程获取到对象的锁后，线程才会继续往下执行
5. 当调用对象的notify方法时，他会随机唤醒对象等待集合(wait set)中的任意一个线程，当某个线程被唤醒后，它就会与其它线程一同竞争对象的锁
6. 当调用对象的notifyAll方法时，它会唤醒该对象等待集合(wait set)中的所有线程，这些线程被唤醒后，又会开始竞争对象的锁
7. 在某一时刻，只有唯一的一个线程能拥有对象的锁



### 实验

1. 编写多线程程序，实现下列目标
   1. 存在一个对象，该对象有一个int类型的成员变量counter，该成员变量的初始值为0
   2. 创建两个线程，其中一个线程对该对象的成员变量counter增1，另一个线程减1
   3. 输出该对象成员变量counter每次变化之后的值
   4. 最终输出结果应为0101010101......
   5. 程序为MyObject.java IncreaseThread.java DecreaseThread.java  Client.java









