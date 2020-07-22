package com.concurrent.test2;

import com.concurrent.test.utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

/**
 * 
 * StampedLock 测试
 * StampedLock 不支持条件变量
 * StampedLock 不支持可重入
 */
public class Test37 {

    public static void main(String[] args) {
        DataContainerStamped dataContainer = new DataContainerStamped(1);
        /**
         * 可以看到实际没有加读锁
         */
        new Thread(() -> {
            dataContainer.read(2);
        }, "t1").start();
        utils.sleep(1);
        new Thread(() -> {
            dataContainer.read(0);
        }, "t2").start();

    }
}

@Slf4j
class DataContainerStamped {
    
    private int data;
    
    final StampedLock lock =  new StampedLock();
    
    public DataContainerStamped(int data){
        this.data = data;
    }
    
    public int read(int readTime){
        long stamp = lock.tryOptimisticRead();

        utils.sleep(readTime);
        log.debug("尝试使用乐观读...{}", stamp);
        if (lock.validate(stamp)){
            log.debug("成功使用了乐观读{},数据 {}", stamp, data);
            return this.data;
        }

        try {
            // 锁升级 - 读锁
            log.debug("乐观读锁升级到写锁加锁 {}", stamp);
            stamp = lock.readLock();
            log.debug("乐观读锁升级到写锁完成 {}", stamp);
            utils.sleep(readTime);
            return data;
        } finally {
            log.debug("乐观读锁升级到写锁解锁 {}", stamp);
            lock.unlock(stamp);
        }

    }
    
    public void write(int data){
        long stamp = lock.writeLock();
        log.info(" 加上写锁");
        try {
            utils.sleep(1);
            this.data = data;
        } finally {
            lock.unlock(stamp);
        }
    }
    
}