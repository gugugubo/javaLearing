package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
/**
 * 测试ReentrantLock为可重入锁，一个线程对lock多次调用lock.lock不报错，说明可以重入
 */
public class Test30 {

    public static void main(String[] args) {
        new Test30().m1();
    }


    Lock lock = new ReentrantLock();


    public void m1 (){

        lock.lock();
        try{
            log.info("m1 is running");
            m2();
        }finally {
            
        }
    }

    public void m2 (){

        lock.lock();
        try{
            // 这个代码可以执行，说明可以重入锁
            log.info("m2 is running");
            m3();
        }finally {

        }
    }

    public void m3 (){

        lock.lock();
        try{
            log.info("m3 is running");
        }finally {

        }
    }
}
