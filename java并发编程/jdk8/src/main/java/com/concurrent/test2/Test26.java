package com.concurrent.test2;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * ScheduledExecutorService的使用之延时执行任务：
 * 这个跟timer不同，这个是可以使用多个线程执行定时任务的
 * 并且其中一个线程里抛出异常不影响另一个线程任务
 */
public class Test26 {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        // 添加两个任务，希望它们都在 1s 后执行
        executor.schedule(() -> {
            System.out.println("任务1，执行时间：" + new Date());
            int i = 1/0;
            System.out.println("任务1");
            try {
                System.out.println("任务1");
                Thread.sleep(2000); } 
            catch (InterruptedException e) {
                System.out.println("xxxxx");
            }
        }, 1000, TimeUnit.MILLISECONDS);
        
        
        executor.schedule(() -> {
            System.out.println("任务2，执行时间：" + new Date());
        }, 1000, TimeUnit.MILLISECONDS);
    }
}
