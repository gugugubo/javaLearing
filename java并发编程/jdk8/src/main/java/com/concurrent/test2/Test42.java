package com.concurrent.test2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

import static com.concurrent.test.utils.sleep;

/**
 * @Author 古春波
 * @Description Semaphore测试
 * @Date 2020/11/16 9:52
 * @Version 1.0
 **/
@Slf4j
public class Test42 {
    public static void main(String[] args) {
        
        
        
        
        // 1. 创建 semaphore 对象
        Semaphore semaphore = new Semaphore(0);


        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                semaphore.release();
            }).start();
        }


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // 2. 10个线程同时运行
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 3. 获取许可
                try {
                    semaphore.acquire();
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    log.debug("running...");
                    sleep(1);
                    log.debug("end...");
                } finally {
                    // 4. 释放许可
                }
            }).start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       
    }
}
