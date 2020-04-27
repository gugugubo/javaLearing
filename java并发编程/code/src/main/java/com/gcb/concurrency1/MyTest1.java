package com.gcb.concurrency1;

public class MyTest1 {
    public static void main(String[] args) throws InterruptedException {
        // 在调用wait方法的时候当前线程要获取当前对象的锁,才能wait,否则会报错,当调用wait方法之后,线程就会释放该对象的锁(monitor).
        // 在调用sleep方法时,线程是不会释放掉对象的锁的.
        Object object = new Object();
        //object.wait();
        /**
         * 以上直接执行object.wait();会报错
         * Exception in thread "main" java.lang.IllegalMonitorStateException
         */

        //改成如下就不会啦
        synchronized (object){
            object.wait();
        }
    }
}
