package com.concurrent.test2;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test32 {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.lock();
    }
}

