package com.concurrent.test2;


import com.concurrent.test.utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledExecutorService的使用之定时执行任务
 */

@Slf4j
public class Test27 {




    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        log.debug("start...");
        // 第一个参数是最开始的时候延迟的时间，第二个参数是执行下一次任务的时间隔，这个时间间隔包括执行打印log.debug("running...");的时间
//        pool.scheduleAtFixedRate(() -> {
//            log.debug("running...");
//        }, 1, 1, TimeUnit.SECONDS);
        
        // 如果睡眠时间被设置为s，那么任务的执行间隔变成了2s
//        pool.scheduleAtFixedRate(() -> {
//            log.debug("running...");
//            utils.sleep(2);
//        }, 1, 1, TimeUnit.SECONDS);


        /**
         * scheduleWithFixedDelay的使用，第二个参数的意思，是执行完
         * log.debug("running...");
         *  utils.sleep(2);
         * 还要再等待1s           
         */
        pool.scheduleWithFixedDelay(() -> {
            log.debug("running...");
            utils.sleep(2);
        }, 1, 1, TimeUnit.SECONDS);
    }
}
