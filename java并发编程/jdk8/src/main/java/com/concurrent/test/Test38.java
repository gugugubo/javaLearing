package com.concurrent.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test38 {

    public static void main(String[] args) {
        AwaitSignal awaitSignal = new AwaitSignal(15);
        Condition aCondition = awaitSignal.newCondition();
        Condition bCondition = awaitSignal.newCondition();
        Condition cCondition = awaitSignal.newCondition();


        new Thread(()->{
            awaitSignal.print("a",aCondition,bCondition);
        },"线程一").start();
        new Thread(()->{
            awaitSignal.print("b",bCondition,cCondition);
        },"线程二").start();
        new Thread(()->{
            awaitSignal.print("c",cCondition,aCondition);
        },"线程三").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        awaitSignal.lock();
        try {
            aCondition.signal();
        }finally {
            awaitSignal.unlock();
        }
    }
    
}


class AwaitSignal extends ReentrantLock{
    
    int loopNumber ;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }
    
    public void print(String str, Condition current , Condition next ){
        for (int i =0;i<loopNumber;i++){
            this.lock();
            try {
                try {
                    current.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(str);
                next.signal();
            }finally {
                this.unlock();
            }
        }
        
    }
    
}