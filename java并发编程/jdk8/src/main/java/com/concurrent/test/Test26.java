package com.concurrent.test;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
/**
 * park 和 upark演示
 */
public class Test26 {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            log.debug("start...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("park...");
            LockSupport.park();
            log.debug("resume...");
        }, "t1");
        thread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();   // interrupt无影响
        boolean interrupted = thread.isInterrupted();
        System.out.println(interrupted);
        log.debug("unpark...");
        LockSupport.unpark(thread);
    }
    
    


}
