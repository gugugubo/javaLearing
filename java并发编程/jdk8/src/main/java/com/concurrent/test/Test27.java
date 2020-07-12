package com.concurrent.test;


import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * 线程状态演示
 */
public class Test27 {
        final static Object obj = new Object();
        public static void main(String[] args) {
            new Thread(() -> {
                synchronized (obj) {
                    log.debug("执行....");
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.debug("其它代码...."); // 断点
                }
            },"t1").start();
            new Thread(() -> {
                synchronized (obj) {
                    log.debug("执行....");
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.debug("其它代码...."); // 断点
                }
            },"t2").start();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("唤醒 obj 上其它线程");
            synchronized (obj) {
                /**
                 * 这 obj.notifyAll();这行代码处设置断点，记得右键断点，将断点的模式设置为“线程”
                 * 然后一步步向下一行执行，可以看到断点在 obj.notifyAll();这一行时，
                 * t1，t2线程都是wait状态（idea中的wait即为waiting；monitor即为blocked状态）；
                 * 断点在 obj.notifyAll();下一行时，t1，t2都为blocked状态，因为主线程还没有释放obj锁。
                 * 断点在 obj.notifyAll();下下一行时，t1，t2一个为blocked状态，一个为running状态，因为是只能有一个竞争到锁
                 */
                obj.notifyAll(); // 唤醒obj上所有等待线程 断点
            }
        }
    
}
