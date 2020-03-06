package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test2 {

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.debug("线程任务");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        // 可以使用lambda表达式呀
        // Runnable runnable = ()->{ log.debug("这里完成线程的任务");};

        new Thread(runnable).start();

        log.debug("主线程");
    }
}
