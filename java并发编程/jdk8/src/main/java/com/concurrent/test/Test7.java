package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test7")
public class Test7 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                log.debug("线程任务执行");
                try {
                    Thread.sleep(10000); // wait, join
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    log.debug("被打断");
                }
            }
        };
        t1.start();
        Thread.sleep(500);
        log.debug("111是否被打断？{}",t1.isInterrupted());
        t1.interrupt();
        log.debug("222是否被打断？{}",t1.isInterrupted());
        Thread.sleep(500);
        log.debug("222是否被打断？{}",t1.isInterrupted());
        log.debug("主线程");
    }
}


/**
 *     public static void main(String[] args) throws InterruptedException {
 *         Thread t1 = new Thread() {
 *             @Override
 *             public void run() {
 *                 log.debug("线程任务执行中");
 *                 try {
 *                     Thread.sleep(10000); // wait, join
 *                     log.debug("线程任务执行中");
 *                 } catch (InterruptedException e) {
 *                     //e.printStackTrace();
 *                     log.debug("被打断");
 *                 }
 *             }
 *         };
 *
 *
 *         Thread thread1 = new Thread(()->{
 *             try {
 *                 t1.start();
 *                 t1.join();
 *             } catch (InterruptedException e) {
 *                 log.debug("111是否被打断？{}",Thread.currentThread().isInterrupted());
 *                 e.printStackTrace();
 *             }
 *
 *         });
 *         thread1.start();
 *         Thread.sleep(500);
 *         thread1.interrupt();
 *         log.debug("222是否被打断？{}",thread1.isInterrupted());
 *         Thread.sleep(500);
 *         log.debug("222是否被打断？{}",thread1.isInterrupted());
 *         log.debug("主线程");
 *     }
 */
