package com.concurrent.test2;

import com.concurrent.test.utils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;


@Slf4j
/**
 * 线程池停止方法测试shutdown  和  shutdownNow
 */
public class Test22 {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<String> future1 = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("task1 running");
                utils.sleep(3);
                log.info("task1 running");
                return "1";
            }
        });    
        
        Future<String> future2 = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("task2 running");
                utils.sleep(2);
                log.info("task2 running");
                return "2";
            }
        });    
        
        Future<String> future3 = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("task3 running");
                utils.sleep(3);
                log.info("task3 running");
                return "3";
            }
        });

        log.info("shutdown");
        
        
        pool.shutdown();


//        List<Runnable> runnables = pool.shutdownNow();  //  任务三处于等待队列中，将会被抛弃, 任务一和任务二被打断

        try {
            // 等待一定时间才执行下面的log.info("{}",runnables);
            // 如果不使用 pool.awaitTermination()方法，那么本来调用停止方法shutdownNow 或者 shutdown
            // 是异步执行的，就是会立即返回
            pool.awaitTermination(1,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        
//        log.info("{}",runnables);
    }
    
    
}
