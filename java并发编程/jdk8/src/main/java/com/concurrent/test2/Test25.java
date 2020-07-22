package com.concurrent.test2;

import com.concurrent.test.utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer 类的使用
 */
@Slf4j
public class Test25 {
    public static void main(String[] args) {
        Timer timer = new Timer();

        TimerTask task1 =new TimerTask() {
            @Override
            public void run() {
                log.debug("task 1");
                // 要是这里抛出异常，线程2直接不能执行
                int i= 1/0;
                utils.sleep(2);
            }
        };


        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 2");
            }
        };
        // 使用 timer 添加两个任务，希望它们都在 1s 后执行
        // 但由于 timer 内只有一个线程来顺序执行队列中的任务，因此『任务1』的延时，影响了『任务2』的执行
        // 要是任务一中抛出异常，线程2直接不能执行
        timer.schedule(task1, 1000);
        timer.schedule(task2, 1000);


    }
}