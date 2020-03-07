package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test13 {

//    static int count = 0;
//    public static void main(String[] args) throws InterruptedException {
//        Thread t1 = new Thread(()->{
//            for (int i = 1;i<5000;i++){
//                count = count +1;
//            }
//        });
//        Thread t2 =new Thread(()->{
//            for (int i = 1;i<5000;i++){
//                count= count -1;
//            }
//        });
//        t1.start();
//        t2.start();
//        t1.join();
//        t2.join();
//        log.debug("count的值是{}",count);
//    }

    /**
     * 加锁后
     */
    static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        Object object = new Object();
        Thread t1 = new Thread(()->{
            for (int i = 1;i<5000;i++){
                synchronized (object){
                count = count +1;
                }
            }
        });
        Thread t2 =new Thread(()->{
            for (int i = 1;i<5000;i++){
                synchronized (object){
                    count= count -1;
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("count的值是{}",count);
    }
}
