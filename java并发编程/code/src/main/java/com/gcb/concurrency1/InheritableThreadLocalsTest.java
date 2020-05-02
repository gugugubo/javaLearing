package com.gcb.concurrency1;


/**
 * InheritableThreadLocal类的使用测试
 */
public class InheritableThreadLocalsTest {

    public static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<String>();

    public static void main(String[] args) {
        threadLocal.set("I am main thread");

        new Thread(()->{
            System.out.println("sub Thread " + threadLocal.get());
        }).start();

        System.out.println("main Thread " + threadLocal.get());
    }
}
