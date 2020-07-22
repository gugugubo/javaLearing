package com.concurrent.test2;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * 查看源码
 */
public class Test36 {


    public static void main(String[] args) {

        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        
        reentrantReadWriteLock.writeLock().lock();
        reentrantReadWriteLock.writeLock().unlock();
        reentrantReadWriteLock.readLock().lock();
        reentrantReadWriteLock.readLock().unlock();


    }
}
