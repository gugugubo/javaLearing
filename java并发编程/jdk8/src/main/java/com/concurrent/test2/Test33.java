package com.concurrent.test2;

import com.concurrent.test.utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 测试ReentrantReadWriteLock读写锁
 */
@Slf4j
public class Test33 {
    public static void main(String[] args) {
        // 读读可以并发
        DataContainer dataContainer = new DataContainer();
        new Thread(() -> {
            dataContainer.read();
        }, "t1").start();
        new Thread(() -> {
            dataContainer.read();
        }, "t2").start();




    }
}

/**
 * 数据容器类
 */
@Slf4j
class DataContainer {
    private Object data;
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock r = rw.readLock();
    private ReentrantReadWriteLock.WriteLock w = rw.writeLock();
    public Object read() {
        log.debug("获取读锁...");
        r.lock();
        try {
            log.debug("读取");
            utils.sleep(1);
            return data;
        } finally {
            log.debug("释放读锁...");
            r.unlock();
        }
    }
    public void write() {
        log.debug("获取写锁...");
        w.lock();
        try {
            log.debug("写入");
            utils.sleep(1);
        } finally {
            log.debug("释放写锁...");
            w.unlock();
        }
    }
}