package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
/**
 * 使用trylock来进行指定的等待锁，如果指定时间还是没有等待到锁，就认为无法获取到锁了
 */
public class Test32 {

    public static void main(String[] args) {

        Lock lock = new ReentrantLock();
        
        Thread thread = new Thread(() -> {
            try {
                if (!lock.tryLock(2, TimeUnit.SECONDS)){
                    log.debug("获取等待指定时间后失败，返回");
                    // 这里如果出了错不要再往下执行了
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.info("被打断啦");
                // 这里如果出了错不要再往下执行了
                return;
            }
            
            
            try{
                log.info("执行完啦，获取到了锁，没被打断");
            }finally {
                lock.unlock();
            }
        }, "thread-1");
        log.info("主线程获取");
        lock.lock();
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("主线程释放锁");
        lock.unlock();

        log.info("主线程执行结束");
    }
    
}
