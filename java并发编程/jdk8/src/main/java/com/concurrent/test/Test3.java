package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class Test3 {

//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        // 实现多线程的第三种方法可以返回数据
//        FutureTask futureTask = new FutureTask<>(new Callable<Integer>() {
//            @Override
//            public Integer call() throws Exception {
//                log.debug("多线程任务");
//                Thread.sleep(100);
//                return 100;
//            }
//        });
//        // 主线程阻塞，同步等待 task 执行完毕的结果
//        new Thread(futureTask,"我的名字").start();
//        log.debug("主线程");
//        log.debug("{}",futureTask.get());
//    }


        public static void main(String[] args) throws InterruptedException {
            //获取主线程
            Thread mainThread = Thread.currentThread();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(6000);
                        System.out.println("主线程状态是：" + mainThread.getState());
                        System.out.println("子线程状态是:" + Thread.currentThread().getState());
                        System.out.println("子线程运行结束");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "子线程");

            thread.start();
            System.out.println("子线程加入主线程，所以主线程要等它一起结束");
            thread.join(10000);
            System.out.println("主线程运行结束");
        }

}
