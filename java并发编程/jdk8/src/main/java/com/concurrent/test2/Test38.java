package com.concurrent.test2;

import com.concurrent.test.utils;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.SampleModel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.LockSupport;


@Slf4j
public class Test38 {

    public static void main(String[] args) throws InterruptedException {
            CountDownLatch latch = new CountDownLatch(3);
            new Thread(() -> {
                log.debug("begin...");
                utils.sleep(1);
                latch.countDown();
                log.debug("end...{}", latch.getCount());
            }).start();
            new Thread(() -> {
                log.debug("begin...");
                utils.sleep(1);
                latch.countDown();
                log.debug("end...{}", latch.getCount());
            }).start();
            new Thread(() -> {
                log.debug("begin...");
                utils.sleep(2);
                latch.countDown();
                log.debug("end...{}", latch.getCount());
            }).start();
            log.debug("waiting...");
            latch.await();
            log.debug("wait end...");

        }
        
        

    }
    


