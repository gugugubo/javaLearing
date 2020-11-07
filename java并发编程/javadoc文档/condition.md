 



































传统上，我们可以通过synchromized关键字+wait+notify/notifyAll来实现多个线程之间的协调和通信，整个过程都是由JVM来帮助我们实现的，开发者无需了解底层的实现细节

从JDK 5开始，并发包提供了Lock，condition(await与signal/signalAll)来实现多个线程之间的协调通信，整个过程都是由开发者来控制的，而且相比于传统的方式，更加灵活，功能也更加强大。

先了解一些interrupt()方法的相关知识：[博客地址](https://www.cnblogs.com/noteless/p/10372826.html#0)

### Condition类的javadoc文档

```
Condition
Condition factors out the Object mon itor methods (wait, notify and notifyAll) into distinct objects to give the effect of having multiple wait-sets per object, by combining them with the use of arbitrary Lock implementations. Where a Lock replaces the use of synchronized methods and statements, a Condition replaces the use of the Object monitor methods.
Condition将对象监视器方法（wait、notify和notifyAll）分解为不同的对象，通过将它们与使用任意Lock实现相结合，使每个对象具有多个wait-sets的效果。当Lock替换同步方法和语句的使用时，Condition将替换对象监视器方法的使用。 Lock->synchronized,Condition->Object monitor

Conditions (also known as condition queues or condition variables) provide a means for one thread to suspend execution (to "wait") until notified by another thread that some state condition may now be true. Because access to this shared state information occurs in different threads, it must be protected, so a lock of some form is associated with the condition. The key property that waiting for a condition provides is that it atomically releases the associated lock and suspends the current thread, just like Object.wait.
Conditions（也称为Conditions队列或Conditions变量）为一个线程提供了一种暂停执行（等待）的方法，直到另一个线程notified认为某些状态条件现在可能为真(跟wait/notify的逻辑是一样的)。由于对共享状态信息的访问发生在不同的线程中，因此必须对其进行保护，因此某种形式的lock与该condition相关联。waiting for a condition提供的关键属性是，它自动释放关联的锁并挂起当前线程，就像Object.wait一样。
A Condition instance is intrinsically bound to a lock. To obtain a Condition instance for a particular Lock instance use its newCondition() method.
Condition实例天然地上绑定到lock。要获取特定Lock实例的Condition实例，请使用其newCondition（）方法。

As an example, suppose we have a bounded buffer which supports put and take methods. If a take is attempted on an empty buffer, then the thread will block until an item becomes available; if a put is attempted on a full buffer, then the thread will block until a space becomes available. We would like to keep waiting put threads and take threads in separate wait-sets so that we can use the optimization of only notifying a single thread at a time when items or spaces become available in the buffer. This can be achieved using two Condition instances.
例如，假设我们有一个有界缓冲区，它支持put和take方法。如果在空缓冲区上尝试执行take，则线程将阻塞，直到某个项变为可用；如果在完全缓冲区上尝试执行put，则线程将阻塞，直到某个空间变为可用。我们希望通过将线程放入不同的wait-sets中来保持put线程和take线程上的waiting，以便在缓冲区中的项目或空间可用时，我们可以使用仅notifying单个线程的优化。这可以通过两个条件Condition来实现。

class BoundedBuffer {
     final Lock lock = new ReentrantLock();
     final Condition notFull  = lock.newCondition(); 
     final Condition notEmpty = lock.newCondition(); 
  
     final Object[] items = new Object[100];
     int putptr, takeptr, count;
  
     public void put(Object x) throws InterruptedException {
       lock.lock();
       try {
         while (count == items.length)
           notFull.await();
         items[putptr] = x;
         if (++putptr == items.length) putptr = 0;
         ++count;
         notEmpty.signal();
       } finally {
         lock.unlock();
       }
     }
  
     public Object take() throws InterruptedException {
       lock.lock();
       try {
         while (count == 0)
           notEmpty.await();
         Object x = items[takeptr];
         if (++takeptr == items.length) takeptr = 0;
         --count;
         notFull.signal();
         return x;
       } finally {
         lock.unlock();
       }
     }
   }
   
   A Condition implementation can provide behavior and semantics that is different from that of the Object monitor methods, such as guaranteed ordering for notifications, or not requiring a lock to be held when performing notifications. If an implementation provides such specialized semantics then the implementation must document those semantics.
Condition实现可以提供不同于对象监视器方法的行为和语义，例如保证通知的顺序，或者在执行通知时不需要持有锁。如果一个implementation提供了这样的专门语义，那么该实现implementation必须记录这些语义。

Note that Condition instances are just normal objects and can themselves be used as the target in a synchronized statement, and can have their own monitor wait and notification methods invoked. Acquiring the monitor lock of a Condition instance, or using its monitor methods, has no specified relationship with acquiring the Lock associated with that Condition or the use of its waiting and signalling methods. It is recommended that to avoid confusion you never use Condition instances in this way, except perhaps within their own implementation.
请注意，Condition实例只是普通对象，它们本身可以用作synchronized语句中的目标，并且可以调用它们自己的监视器等待和通知方法。获取Condition实例的监视锁或使用其监视方法与获取与该Condition相关联的Lock或使用其waiting和signalling方法没有指定的关系。为了避免混淆，建议您不要以这种方式使用条件实例，除非可能是在它们自己的实现中。

Except where noted, passing a null value for any parameter will result in a NullPointerException being thrown.
除非另有说明，否则为任何参数传递空值将导致引发NullPointerException。

```



```

Implementation Considerations
When waiting upon a Condition, a "spurious wakeup" is permitted to occur, in general, as a concession to the underlying platform semantics. This has little practical impact on most application programs as a Condition should always be waited upon in a loop, testing the state predicate that is being waited for. An implementation is free to remove the possibility of spurious wakeups but it is recommended that applications programmers always assume that they can occur and so always wait in a loop.
在等待条件时，通常允许出现“虚假唤醒”，作为对底层平台语义的让步。这对大多数应用程序几乎没有实际影响，因为应该始终在循环中等待Condition和测试正在等待的状态条件是否被满足。实现可以自由地消除虚假唤醒的可能性，但建议应用程序程序员始终假设它们可能发生，因此始终在循环中等待。

The three forms of condition waiting (interruptible, non-interruptible, and timed) may differ in their ease of implementation on some platforms and in their performance characteristics. In particular, it may be difficult to provide these features and maintain specific semantics such as ordering guarantees. Further, the ability to interrupt the actual suspension of the thread may not always be feasible to implement on all platforms.
condition等待的三种形式（可中断、不可中断和定时）在某些平台上的易实现性和性能特征上可能有所不同。特别是，可能很难提供这些特性并维护例如排序保证这样的特定语义。此外，在所有平台上实现中断线程实际挂起的能力并不总是可行的。
Consequently, an implementation is not required to define exactly the same guarantees or semantics for all three forms of waiting, nor is it required to support interruption of the actual suspension of the thread.
因此，实现不需要为所有三种等待形式定义完全相同的保证或语义，也不需要支持中断线程的实际挂起。

An implementation is required to clearly document the semantics and guarantees provided by each of the waiting methods, and when an implementation does support interruption of thread suspension then it must obey the interruption semantics as defined in this interface.
implementation需要清楚地记录每个等待方法提供的语义和保证，当implementation确实支持中断线程挂起时，它必须遵守此接口中定义的中断语义。
As interruption generally implies cancellation, and checks for interruption are often infrequent, an implementation can favor responding to an interrupt over normal method return. This is true even if it can be shown that the interrupt occurred after another action that may have unblocked the thread. An implementation should document this behavior.
由于中断通常意味着取消，并且对中断的检查通常是不经常的，所以实现可能倾向于响应中断，而不是普通的方法返回。即使可以显示中断发生在另一个可能已解除阻止线程的操作之后也是如此。实现implementation应该记录此行为。
```





### await方法的javadoc文档

```
Causes the current thread to wait until it is signalled or interrupted.
使当前线程等待，直到发出signalled或中断为止。
The lock associated with this Condition is atomically released and the current thread becomes disabled for thread scheduling purposes and lies dormant until one of four things happens:
Some other thread invokes the signal method for this Condition and the current thread happens to be chosen as the thread to be awakened; or
Some other thread invokes the signalAll method for this Condition; or
Some other thread interrupts the current thread, and interruption of thread suspension is supported; or
A "spurious wakeup" occurs.
与此Condition关联的锁是原子释放的，当前线程出于线程调度目的将被禁用，并处于休眠状态，直到发生以下四种情况之一：
1.其他一些线程为此Condition调用signal方法，而当前线程恰好被选为要唤醒的线程；或者
2.其他一些线程为此Condition调用signalAll方法；或
3.其他一些线程中断当前线程，并且支持中断线程挂起；或者
4.出现“虚假唤醒”。

In all cases, before this method can return the current thread must re-acquire the lock associated with this condition. When the thread returns it is guaranteed to hold this lock.
在所有情况下，在该方法返回之前，当前线程必须重新获取与此condition关联的锁。当线程返回时，它保证持有这个锁。
If the current thread:
has its interrupted status set on entry to this method; or
is interrupted while waiting and interruption of thread suspension is supported,
then InterruptedException is thrown and the current thread's interrupted status is cleared. It is not specified, in the first case, whether or not the test for interruption occurs before the lock is released.
如果当前线程：
1.在进入此方法时设置了中断状态；或
2.等待时被中断，支持中断线程挂起，
那么就会抛出InterruptedException并清除当前线程的中断状态。在第一种情况下，未规定是否在释放锁之前要进行中断测试。

Implementation Considerations
实施注意事项
The current thread is assumed to hold the lock associated with this Condition when this method is called. It is up to the implementation to determine if this is the case and if not, how to respond. Typically, an exception will be thrown (such as IllegalMonitorStateException) and the implementation must document that fact.
调用此方法时，假定当前线程持有与此Condition关联的锁。由实现来决定是否是这样，如果不是，如何应对。通常，将抛出异常（例如IllegalMonitorStateException），实现必须记录该事实。

An implementation can favor responding to an interrupt over normal method return in response to a signal. In that case the implementation must ensure that the signal is redirected to another waiting thread, if there is one.
实现可以支持响应中断，而不是响应信号的正常方法返回。在这种情况下，实现必须确保信号被重定向到另一个等待线程（如果有）。
```



### awaitUninterruptibly方法的javadoc文档

```
Causes the current thread to wait until it is signalled.
使当前线程等待直到发出信号。

The lock associated with this condition is atomically released and the current thread becomes disabled for thread scheduling purposes and lies dormant until one of three things happens:
Some other thread invokes the signal method for this Condition and the current thread happens to be chosen as the thread to be awakened; or
Some other thread invokes the signalAll method for this Condition; or
A "spurious wakeup" occurs.
与此条件关联的锁是原子释放的，当前线程出于线程调度目的将被禁用，并处于休眠状态，直到发生以下三种情况之一：
1.其他一些线程为此condition调用signal方法，而当前线程恰好被选为要唤醒的线程；或者
2.其他一些线程为此condition调用signalAll方法；或
3.出现“虚假唤醒”。

In all cases, before this method can return the current thread must re-acquire the lock associated with this condition. When the thread returns it is guaranteed to hold this lock.
在所有情况下，在该方法返回之前，当前线程必须重新获取与此条件关联的锁。当线程返回时，它保证保持这个锁。
If the current thread's interrupted status is set when it enters this method, or it is interrupted while waiting, it will continue to wait until signalled. When it finally returns from this method its interrupted status will still be set.
如果当前线程在进入此方法时设置了中断状态，或者在等待时中断，则它将继续等待，直到收到signalled。当它最终从该方法返回时，它的中断状态仍将被设置。
Implementation Considerations
实施注意事项
The current thread is assumed to hold the lock associated with this Condition when this method is called. It is up to the implementation to determine if this is the case and if not, how to respond. Typically, an exception will be thrown (such as IllegalMonitorStateException) and the implementation must document that fact.
调用此方法时，假定当前线程持有与此Condition关联的锁。由实现来决定是否是这样，如果不是，如何应对。通常，将抛出异常（例如IllegalMonitorStateException），实现必须记录该事实。
```

### awaitNanos(long nanosTimeout)方法的javadoc文档

```


Causes the current thread to wait until it is signalled or interrupted, or the specified waiting time elapses.
使当前线程等待，直到发出信号或被中断，或者指定的等待时间结束。

The lock associated with this condition is atomically released and the current thread becomes disabled for thread scheduling purposes and lies dormant until one of five things happens:
Some other thread invokes the signal method for this Condition and the current thread happens to be chosen as the thread to be awakened; or
Some other thread invokes the signalAll method for this Condition; or
Some other thread interrupts the current thread, and interruption of thread suspension is supported; or
The specified waiting time elapses; or
A "spurious wakeup" occurs.
与此条件关联的锁是原子释放的，当前线程出于线程调度目的将被禁用，并处于休眠状态，直到发生以下五种情况之一：
1.其他一些线程为此condition调用signal方法，而当前线程恰好被选为要唤醒的线程；或者
2.其他一些线程为此condition调用signalAll方法；或
3.其他一些线程中断当前线程，并且支持中断线程挂起；或者
4.规定的等待时间已过；或
5.出现“虚假唤醒”。

In all cases, before this method can return the current thread must re-acquire the lock associated with this condition. When the thread returns it is guaranteed to hold this lock.
在所有情况下，在该方法返回之前，当前线程必须重新获取与此condition关联的锁。当线程返回时，它保证保持这个锁。

If the current thread:
has its interrupted status set on entry to this method; or
is interrupted while waiting and interruption of thread suspension is supported,
如果当前线程：
在进入此方法时设置了中断状态；或
等待时中断，支持中断线程挂起，
then InterruptedException is thrown and the current thread's interrupted status is cleared. It is not specified, in the first case, whether or not the test for interruption occurs before the lock is released.
那么抛出InterruptedException并清除当前线程的中断状态。在第一种情况下，未规定是否在释放锁之前要进行中断测试。

The method returns an estimate of the number of nanoseconds remaining to wait given the supplied nanosTimeout value upon return, or a value less than or equal to zero if it timed out. This value can be used to determine whether and how long to re-wait in cases where the wait returns but an awaited condition still does not hold. Typical uses of this method take the following form:
该方法返回给定返回时间nanosTimeout值的剩余等待纳秒数的估计值，如果超时则返回小于或等于零的值。此值可用于确定在等待返回但等待的条件仍然不成立的情况下是否重新等待以及重新等待多长时间。这种方法的典型用途如下：
boolean aMethod(long timeout, TimeUnit unit) {
   long nanos = unit.toNanos(timeout);
   lock.lock();
   try {
     while (!conditionBeingWaitedFor()) {
       if (nanos <= 0L)
         return false;
       nanos = theCondition.awaitNanos(nanos);
     }
     // ...
   } finally {
     lock.unlock();
   }
 }
Design note: This method requires a nanosecond argument so as to avoid truncation errors in reporting remaining times. Such precision loss would make it difficult for programmers to ensure that total waiting times are not systematically shorter than specified when re-waits occur.
设计说明：此方法需要纳秒参数，以避免在报告剩余时间时出现截断错误。这样的精度损失将使程序员很难确保在发生重新等待时，总等待时间不会系统地短于指定的时间。

Implementation Considerations
实施注意事项
The current thread is assumed to hold the lock associated with this Condition when this method is called. It is up to the implementation to determine if this is the case and if not, how to respond. Typically, an exception will be thrown (such as IllegalMonitorStateException) and the implementation must document that fact.
调用此方法时，假定当前线程持有与此条件关联的锁。由实现来决定是否是这样，如果不是，如何应对。通常，将抛出异常（例如IllegalMonitorStateException），实现必须记录该事实。

An implementation can favor responding to an interrupt over normal method return in response to a signal, or over indicating the elapse of the specified waiting time. In either case the implementation must ensure that the signal is redirected to another waiting thread, if there is one.
实现可以有利于响应中断，而不是响应信号的正常方法返回，或者有利于指示指定等待时间的流逝。无论哪种情况，实现都必须确保信号重定向到另一个等待线程（如果有）。
```



### signal方法的javadoc文档

```
Wakes up one waiting thread.
唤醒一条等待的线。

If any threads are waiting on this condition then one is selected for waking up. That thread must then re-acquire the lock before returning from await.
如果任何线程正在等待此condition，则选择一个线程唤醒。然后，该线程必须在从await返回之前重新获取锁。

Implementation Considerations
实施注意事项
An implementation may (and typically does) require that the current thread hold the lock associated with this Condition when this method is called. Implementations must document this precondition and any actions taken if the lock is not held. Typically, an exception such as IllegalMonitorStateException will be thrown.
当调用此方法时，实现可能（并且通常需要）要求当前线程持有与此condition相关联的锁。实现必须记录此前提条件以及在未持有锁的情况下所采取的任何操作。通常，将引发一个异常，如IllegalMonitorStateException。
```



### signalAll方法的javadoc文档

```


Wakes up all waiting threads.                                                                  
唤醒所有等待的线程。
If any threads are waiting on this condition then they are all woken up. Each thread must re-acquire the lock before it can return from await.
如果有线程在此condition下等待，那么它们都会被唤醒。每个线程必须重新获取锁，然后才能从await返回。

Implementation Considerations
实施注意事项
An implementation may (and typically does) require that the current thread hold the lock associated with this Condition when this method is called. Implementations must document this precondition and any actions taken if the lock is not held. Typically, an exception such as IllegalMonitorStateException will be thrown.
当调用此方法时，实现可能（并且通常需要）要求当前线程持有与此条件相关联的锁。实现必须记录此前提条件以及在未持有锁的情况下所采取的任何操作。通常，将引发一个异常，如IllegalMonitorStateException。 
```

