

### Lock的javadoc文档

```



Lock implementations provide more extensive locking operations than can be obtained using synchronized methods and statements. They allow more flexible structuring, may have quite different properties, and may support multiple associated Condition objects.
Lock的实现提供了比使用同步方法和语句更广泛的锁操作。它们允许更灵活的结构，可能具有完全不同的属性，并且可能支持多个关联的Condition对象。 

A lock is a tool for controlling access to a shared resource by multiple threads. Commonly, a lock provides exclusive access to a shared resource: only one thread at a time can acquire the lock and all access to the shared resource requires that the lock be acquired first. However, some locks may allow concurrent access to a shared resource, such as the read lock of a ReadWriteLock.
lock是一种工具，用于控制多个线程对共享资源的访问。通常，lock提供对共享资源的独占访问：一次只有一个线程可以获取锁，对共享资源的所有访问都要求首先获取lock。但是，某些lock可能允许并发访问共享资源，例如ReadWriteLock的读锁。

The use of synchronized methods or statements provides access to the implicit monitor lock associated with every object, but forces all lock acquisition and release to occur in a block-structured way: when multiple locks are acquired they must be released in the opposite order, and all locks must be released in the same lexical scope in which they were acquired.
使用synchronized方法或语句可以访问与每个对象关联的隐式监视锁，但会强制以块结构(block-structured)的方式执行所有锁的获取和释放：当获取多个锁时，它们必须按相反的顺序释放，并且所有锁必须在它们所在的同一词法范围内释放，这个词法范围是获取锁时候的词法范围。

While the scoping mechanism for synchronized methods and statements makes it much easier to program with monitor locks, and helps avoid many common programming errors involving locks, there are occasions where you need to work with locks in a more flexible way. For example, some algorithms for traversing concurrently accessed data structures require the use of "hand-over-hand" or "chain locking": you acquire the lock of node A, then node B, then release A and acquire C, then release B and acquire D and so on. Implementations of the Lock interface enable the use of such techniques by allowing a lock to be acquired and released in different scopes, and allowing multiple locks to be acquired and released in any order.
虽然同步方法和语句的作用域机制使使用监视器锁编程更容易，并有助于避免许多涉及锁的常见编程错误，但有时需要以更灵活的方式使用锁。例如，一些遍历并发访问数据结构的算法需要使用“hand-over-hand”或“chain locking”：先获取节点A的锁，然后获取节点B，然后释放A和获取C，然后释放B和获取D，等等。Lock接口的实现允许在不同的作用域中获取和释放锁，并允许以任何顺序获取和释放多个锁，从而允许使用此类技术。

With this increased flexibility comes additional responsibility. The absence of block-structured locking removes the automatic release of locks that occurs with synchronized methods and statements. In most cases, the following idiom should be used:
随着这种灵活性的增加，也带来了额外的责任。缺少block-structured锁定将失去同步方法和语句所发生的锁的自动释放的功能。在大多数情况下，应使用以下成语：MyTest6.java
Lock l = ...;
 l.lock();
 try {
   // access the resource protected by this lock
 } finally {
   l.unlock();
 }
 
 When locking and unlocking occur in different scopes, care must be taken to ensure that all code that is executed while the lock is held is protected by try-finally or try-catch to ensure that the lock is released when necessary.
当锁定和解锁发生在不同的作用域中时，必须注意确保在锁定期间执行的所有代码都受到try finally或try catch的保护，以确保在必要时释放lock。  MyTest6.java

Lock implementations provide additional functionality over the use of synchronized methods and statements by providing a non-blocking attempt to acquire a lock (tryLock()), an attempt to acquire the lock that can be interrupted (lockInterruptibly, and an attempt to acquire the lock that can timeout (tryLock(long, TimeUnit)).
Lock的实现可以1.通过非阻塞式的尝试获取锁的操作，（tryLock()）、2.可以被中断的尝试获取锁的操作（lockInterruptibly，3.可以设置超时时间的尝试获取锁的操作（tryLock（long，TimeUnit））


A Lock class can also provide behavior and semantics that is quite different from that of the implicit monitor lock, such as guaranteed ordering, non-reentrant usage, or deadlock detection. If an implementation provides such specialized semantics then the implementation must document those semantics.
Lock类还可以提供与隐式监视锁完全不同的行为和语义，例如保证顺序、不可重入使用或死锁检测。如果一个实现提供了这样的专门语义，那么该实现必须记录这些语义。 

Note that Lock instances are just normal objects and can themselves be used as the target in a synchronized statement. Acquiring the monitor lock of a Lock instance has no specified relationship with invoking any of the lock methods of that instance. It is recommended that to avoid confusion you never use Lock instances in this way, except within their own implementation.
请注意，Lock实例只是普通对象，它们本身可以用作synchronized语句中的目标。获取Lock实例的监视器锁与调用该实例（Lock实例）的任何lock方法都没有指定的关系。为了避免混淆，建议您不要以这种方式使用锁实例，除非在它们自己的实现中。

Except where noted, passing a null value for any parameter will result in a NullPointerException being thrown.
除非另有说明，否则为任何参数传递空值将导致引发NullPointerException。
```



```
All Lock implementations must enforce the same memory synchronization semantics as provided by the built-in monitor lock, as described in The Java Language Specification (17.4 Memory Model) :
所有Lock实现都必须强制与内置监视器锁提供的相同内存同步语义，如Java语言规范（17.4内存模型）中所述：
A successful lock operation has the same memory synchronization effects as a successful Lock action.
成功的lock操作与成功的锁定操作具有相同的内存同步效果。
A successful unlock operation has the same memory synchronization effects as a successful Unlock action.
成功的解锁操作与成功的解锁操作具有相同的内存同步效果。
Unsuccessful locking and unlocking operations, and reentrant locking/unlocking operations, do not require any memory synchronization effects.
不成功的锁定和解锁操作以及可重入的锁定/解锁操作不需要任何内存同步效果。


Implementation Considerations
实施注意事项
The three forms of lock acquisition (interruptible, non-interruptible, and timed) may differ in their performance characteristics, ordering guarantees, or other implementation qualities. Further, the ability to interrupt the ongoing acquisition of a lock may not be available in a given Lock class. Consequently, an implementation is not required to define exactly the same guarantees or semantics for all three forms of lock acquisition, nor is it required to support interruption of an ongoing lock acquisition. An implementation is required to clearly document the semantics and guarantees provided by each of the locking methods. It must also obey the interruption semantics as defined in this interface, to the extent that interruption of lock acquisition is supported: which is either totally, or only on method entry.
锁获取的三种形式（可中断、不可中断和定时）在性能特征、顺序保证或其他实现质量方面可能有所不同。此外，在给定的Lock类中，中断正在进行的锁的获取的能力可能不可用。因此，一个实现implementation不需要为所有三种锁获取形式定义完全相同的保证或语义，也不需要支持正在进行的锁获取的中断。一个实现implementation需要清楚地记录每个锁定方法提供的语义和保证。它还必须遵守此接口中定义的中断语义，来支持锁获取的中断：要么完全中断，要么仅中断方法入口。

As interruption generally implies cancellation, and checks for interruption are often infrequent, an implementation can favor responding to an interrupt over normal method return. This is true even if it can be shown that the interrupt occurred after another action may have unblocked the thread. An implementation should document this behavior.
由于中断通常意味着取消，并且对中断的检查通常是不经常的，所以实现implementation可能倾向于响应中断，而不是普通的方法返回。即使可以显示在另一个操作可能已解除阻止线程之后发生的中断也是如此。implementation应该记录此行为。
```



### lock方法的javadoc文档

```
Acquires the lock.
If the lock is not available then the current thread becomes disabled for thread scheduling purposes and lies dormant until the lock has been acquired.
如果锁不可用，则当前线程将被禁用以进行线程调度，并处于休眠状态，直到获取锁为止。MyTest6.java

Implementation Considerations
实施注意事项
A Lock implementation may be able to detect erroneous use of the lock, such as an invocation that would cause deadlock, and may throw an (unchecked) exception in such circumstances. The circumstances and the exception type must be documented by that Lock implementation.
Lock实现可能能够检测到锁的错误使用，例如可能导致死锁的调用，并且在这种情况下可能抛出（未经检查的）异常。该Lock实现必须记录环境和异常类型。
```



### lockInterruptibly的javadoc文档

```
Acquires the lock unless the current thread is interrupted.
获取锁，除非当前线程被中断。
Acquires the lock if it is available and returns immediately.
获取锁（如果可用）并立即返回。

If the lock is not available then the current thread becomes disabled for thread scheduling purposes and lies dormant until one of two things happens:
如果锁不可用，则当前线程将被禁用以进行线程调度，并处于休眠状态，直到发生以下两种情况之一：
1.The lock is acquired by the current thread; or
锁由当前线程获取；或
2.Some other thread interrupts the current thread, and interruption of lock acquisition is supported.
其他一些线程会中断当前线程，并且支持锁获取被中断。

If the current thread:
如果当前线程：
has its interrupted status set on entry to this method; or
在进入此方法时设置了中断状态；或
is interrupted while acquiring the lock, and interruption of lock acquisition is supported,
获取锁时中断，支持锁获取中断，
then InterruptedException is thrown and the current thread's interrupted status is cleared.
然后抛出InterruptedException并清除当前线程的中断状态。

Implementation Considerations
实施注意事项
The ability to interrupt a lock acquisition in some implementations may not be possible, and if possible may be an expensive operation. The programmer should be aware that this may be the case. An implementation should document when this is the case.
在某些实现中中断锁获取的能力可能是不可能的，并且如果可能的话可能是一个昂贵的操作。程序员应该意识到情况可能是这样的。实现implementation应该记录下来这种情况。
An implementation can favor responding to an interrupt over normal method return.
实现要支持响应中断，而不是普通的方法返回。

A Lock implementation may be able to detect erroneous use of the lock, such as an invocation that would cause deadlock, and may throw an (unchecked) exception in such circumstances. The circumstances and the exception type must be documented by that Lock implementation.
锁实现可能能够检测到锁的错误使用，例如可能导致死锁的调用，并且在这种情况下可能抛出（未经检查的）异常。该锁实现必须记录环境和异常类型。
```



### trylock方法的javadoc文档

```
Acquires the lock only if it is free at the time of invocation.
只有在调用时锁是空闲的情况下才获取锁。

Acquires the lock if it is available and returns immediately with the value true. If the lock is not available then this method will return immediately with the value false.
获取锁（如果可用），并立即返回值true。如果锁不可用，则此方法将立即返回值false。
A typical usage idiom for this method would be:
这种方法的典型用法是：
Lock lock = ...;
 if (lock.tryLock()) {
   try {
     // manipulate protected state
   } finally {
     lock.unlock();
   }
 } else {
   // perform alternative actions
 }
This usage ensures that the lock is unlocked if it was acquired, and doesn't try to unlock if the lock was not acquired.
以上用法确保在获取锁时将其解锁，并且在未获取锁时不会尝试解锁。
```



### tryLock(long time, TimeUnit unit)方法的javadoc文档

MyTest7.java

```
Acquires the lock if it is free within the given waiting time and the current thread has not been interrupted.
如果锁在给定的等待时间内空闲且当前线程未被中断，则获取该锁。

If the lock is available this method returns immediately with the value true. If the lock is not available then the current thread becomes disabled for thread scheduling purposes and lies dormant until one of three things happens:
如果锁可用，则此方法立即返回值为true的值。如果锁不可用，则当前线程将被不能用以进行线程调度，并处于休眠状态，直到发生以下三种情况之一：
1.The lock is acquired by the current thread; or
锁由当前线程获取；或
2.Some other thread interrupts the current thread, and interruption of lock acquisition is supported; or
其他一些线程中断当前线程，并且支持中断锁获取；或者
3.The specified waiting time elapses
指定的等待时间已过

If the lock is acquired then the value true is returned.
如果获取了锁，则返回值true。
If the current thread:
has its interrupted status set on entry to this method; or
is interrupted while acquiring the lock, and interruption of lock acquisition is supported,then InterruptedException is thrown and the current thread's interrupted status is cleared.
如果当前线程：
1.在进入此方法时设置了中断状态；或
2.获取锁时中断，支持锁获取过程中的中断，然后抛出InterruptedException并清除当前线程的中断状态。
If the specified waiting time elapses then the value false is returned. If the time is less than or equal to zero, the method will not wait at all.
如果指定的等待时间已过，则返回值false。如果时间小于或等于零，则该方法根本不会等待。

Implementation Considerations
实施注意事项
The ability to interrupt a lock acquisition in some implementations may not be possible, and if possible may be an expensive operation. The programmer should be aware that this may be the case. An implementation should document when this is the case.
在某些实现中中断锁获取的能力可能是不可能的，并且如果可能的话可能是一个昂贵的操作。程序员应该意识到情况可能是这样的。在这种情况下，实现应该记录下来。
An implementation can favor responding to an interrupt over normal method return, or reporting a timeout.
implementation可以支持响应中断，而不是普通的方法返回，也不是报告timeout。
A Lock implementation may be able to detect erroneous use of the lock, such as an invocation that would cause deadlock, and may throw an (unchecked) exception in such circumstances. The circumstances and the exception type must be documented by that Lock implementation.
Lock实现可能能够检测到锁的错误使用，例如可能导致死锁的调用，并且在这种情况下可能抛出（未经检查的）异常。该锁实现必须记录环境和异常类型。
```





### newCondition方法的javadoc文档

```
Returns a new Condition instance that is bound to this Lock instance.
返回绑定到此Lock实例的Condition实例。

Before waiting on the condition the lock must be held by the current thread. A call to Condition.await() will atomically release the lock before waiting and re-acquire the lock before the wait returns.
在等待condition之前，锁必须由当前线程持有。A 调用Condition.wait()将在等待之前自动释放锁，并在等待返回之前重新获取锁。

Implementation Considerations
实施注意事项
The exact operation of the Condition instance depends on the Lock implementation and must be documented by that implementation.
Condition实例的确切操作取决于Lock实现，并且必须由该实现implementation记录。 
```



### Lock与synchronized在锁处理上的区别

1. 锁的获取方式：前者是通过程序代码的方式由开发者手动获取，后者是通过JVM来获取无需开发者干预
2. 具体的实现方式：前者是通过Java代码的方式来实现，后者是通过JVM底层来实现无需开发者关注
3. 锁的释放方式：前者务必通过开发者在funall块中调用unlock方法释放，后者会自动释放无需开发者关注
4. 锁的具体类型：前者提供了多种如公平锁，分公平锁，后者与前者都提供了可重入锁















