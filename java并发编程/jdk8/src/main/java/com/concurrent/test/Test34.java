package com.concurrent.test;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
/**
 * 等待烟和等待外卖的线程分别在等待不同的两个资源，所以可以使用不同的条件变量
 * 使用ReentrantLock可以轻易解决synchronized只有一个等待集合waitset的问题
 * 1. await 前需要获得锁
 * 2. await 执行后，会释放锁，进入 conditionObject 等待
 * 3. await 的线程被唤醒（或打断、或超时）取重新竞争 lock 锁，执行唤醒的线程爷必须先获得锁
 * 4. 竞争 lock 锁成功后，从 await 后继续执行
 */
public class Test34 {
    static Lock lock = new ReentrantLock();
    static Condition waitCigaretteQueue = lock.newCondition();
    static Condition waitBreakfastQueue = lock.newCondition();
    static volatile boolean hasCigarette = false;
    static volatile  boolean hasBreakfast = false;

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            lock.lock();
            try {
                while (!hasCigarette){
                    try {
                        waitCigaretteQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("等到了它的烟");
            }finally {
                lock.unlock();
            }
        }, "等烟线程");
        thread.start();


        Thread thread1 = new Thread(() -> {
            try {
                lock.lock();
                while (!hasBreakfast){
                    try {
                        waitBreakfastQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("等到了它的早餐");
            }finally {
                lock.unlock();
            }
        }, "等待早餐线程");

        thread1.start();


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }   
        
//        new Thread(()->{
//            lock.lock();
//            try{
//                log.info("烟来了");
//                // 因为上面只有一个线程在等待，所以只用发送一个signal就行了，不用signalAll
//                hasCigarette = true;
//                waitCigaretteQueue.signal();
//            }finally {
//                lock.unlock();
//            }
//        },"送烟线程").start();
        
        
        
        sendCigarette();
    
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        new Thread(()->{
//            lock.lock();
//            try {
//                log.info("送早餐来了");
//                hasBreakfast = true;
//                waitBreakfastQueue.signal();
//            }finally {
//                lock.unlock();
//            }
//        },"送早餐程").start();
        sendBreakfast();
    }
    
    
    public static void sendCigarette(){
        // 必须先获取锁
        lock.lock();
        try{
            log.info("烟来了");
            // 因为上面只有一个线程在等待，所以只用发送一个signal就行了，不用signalAll
            hasCigarette = true;
            waitCigaretteQueue.signal();
        }finally {
            lock.unlock();
        }
    }




    public static void sendBreakfast(){
        lock.lock();
        try {
            log.info("送早餐来了");
            hasBreakfast = true;
            waitBreakfastQueue.signal();
        }finally {
            lock.unlock();
        }
    }
}
