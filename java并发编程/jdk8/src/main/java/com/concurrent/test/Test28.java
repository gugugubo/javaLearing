package com.concurrent.test;


import lombok.extern.slf4j.Slf4j;

@Slf4j
/*
死锁示例
 */
public class Test28 {

    public static void main(String[] args) {
        Object o = new Object();
        Object o1 = new Object();

        new Thread(()->{
            synchronized (o){
                log.info("线程一占有对象o的锁");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                synchronized (o1){
                    log.info("线程一占有对象o1的锁");
                }

            }
        },"线程1").start();

        new Thread(()->{
            synchronized (o1){
                log.info("线程二占有对象o1的锁");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (o){
                    log.info("线程一占有对象o的锁");
                }

            }
        },"线程二").start();
    }
    
    
    
}
