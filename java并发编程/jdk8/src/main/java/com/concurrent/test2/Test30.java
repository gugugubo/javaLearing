package com.concurrent.test2;

import com.concurrent.test.utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


@Slf4j
/**
 * 自定义阻塞锁
 */
public class Test30 {
    public static void main(String[] args) {
        MyLock lock = new MyLock();
        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking...");
                utils.sleep(1);
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t1").start();
        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking...");
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t2").start();
    }
    
    
}


/**
 * 自定义锁,有了自定义同步器，很容易复用 AQS ，实现一个功能完备的自定义锁
 */
class MyLock implements Lock {

    static MySync mySync = new MySync();

    // 尝试，不成功，进入等待队列
    @Override
    public void lock() {
        mySync.acquire(1);
    }

    // 尝试，不成功，进入等待队列，可打断
    @Override
    public void lockInterruptibly() throws InterruptedException {
        mySync.acquireInterruptibly(1);
    }

    // 尝试一次，不成功返回，不进入队列
    @Override
    public boolean tryLock() {
        return mySync.tryAcquire(1);
    }

    // 尝试，不成功，进入等待队列，有时限
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return mySync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        // release方法里面调用tryRelease
        mySync.release(1);
    }

    @Override
    public Condition newCondition() {
        return mySync.newCondition();
    }
}

// 自定义同步器
class MySync extends AbstractQueuedSynchronizer{
    /**
     * 获取锁
     * @param arg  这个参数是我们这里没使用，都这只为1，
     * @return
     */
    @Override
    protected boolean tryAcquire(int arg) {
        if (compareAndSetState(0,1)){
            // 设置持有锁的线程
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    /**
     * 释放锁
     * @param arg  锁状态 
     * @return
     */
    @Override
    protected boolean tryRelease(int arg) {
        // 这里不用加锁，因为只有不存在线程竞争，只有持有锁的线程才活着才调用此方法
        if (compareAndSetState(1,0)){
            setExclusiveOwnerThread(null);
            // setState(0); 语句放在 setExclusiveOwnerThread(null);之后，因为state有volatile修饰，可以保证可见性
            setState(0);
            return true;
        }
        return false;
    }
    
    public Condition newCondition(){
        return new ConditionObject();
    }

    // 判断锁的状态
    @Override
    protected boolean isHeldExclusively() {
        return getState()==1;
    }
}


