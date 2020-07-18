package com.concurrent.test2;

import com.concurrent.test.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试使用lock.lock加锁时线程被打断时的效果
 */
public class Test31 {


    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
    

        new Thread(()->{
            lock.lock();
            try{
                utils.sleep(20);
            }finally {
                lock.unlock();
            }
        }).start();

        new Thread(()->{
            lock.lock();
            try{
                utils.sleep(20);
            }finally {
                lock.unlock();
            }
        },"线程一").start();

        new Thread(()->{
            lock.lock();
            try{
                utils.sleep(20);
            }finally {
                lock.unlock();
            }
        },"线程一").start();

        Thread t1 = new Thread(()->{
            lock.lock();
            try{
//                utils.sleep(20);
                System.out.println(1111);
            }finally {
                lock.unlock();
            }
        },"线程一");
        t1.start();
        
        t1.interrupt();
        
        

        
    }
    
}
