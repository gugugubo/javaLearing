package com.concurrent.test2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用Executors的newFixedThreadPool工厂方法来创建线程池测试
 */
@Slf4j
public class Test19 {

    public static void main(String[] args) {
//        ExecutorService pool = Executors.newFixedThreadPool(2);
        // 我们可以使用另一个带有线程工厂参数的构造方法
        //public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory)


        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger t = new AtomicInteger(1);
            /**
             * 设置如何线程工厂如何新建线程
             * @param r
             * @return
             */
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"Mythread_T" + t.getAndIncrement());
            }
        });

        pool.execute(()->{
            log.info("1");
        }); 
        pool.execute(()->{
            log.info("2");
        }); 
        pool.execute(()->{
            log.info("3");
        });
        
        
        
        
        
    }
}
