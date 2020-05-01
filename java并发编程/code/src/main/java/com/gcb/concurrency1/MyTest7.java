package com.gcb.concurrency1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * trylock方法的使用
 */
public class MyTest7 {

    private Lock lock = new ReentrantLock();

    public void menthod1(){
        try {
            lock.lock();
            System.out.println("mehtod1 is running");
        }finally {
//            lock.unlock();
        }
    }

    public void menthod2(){
//        try {
//            lock.lock();
//            System.out.println("mehtod2 is running");
//        }finally {
//            lock.unlock();
//        }
        boolean result =false;
        try {
            lock.tryLock(800, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result){
            System.out.println("mehtod2 get the lock");
        }else {
            System.out.println("mehtod2 can't get the lock");
        }
    }

    public static void main(String[] args) {
        MyTest7 myTest6 = new MyTest7();

        Thread thread1 = new Thread(()->{
            for(int i=0;i<10;i++){
                myTest6.menthod1();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(()->{
            for(int i=0;i<10;i++){
                myTest6.menthod2();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
        //程序的执行结果是mehtod1 is running与mehtod2 is running交替出现，因为我进行了手动的释放锁操作
        //如果将mehtod2方法中的lock.unlock去掉那么执行结果是只出现mehtod1 is running
    }
}
