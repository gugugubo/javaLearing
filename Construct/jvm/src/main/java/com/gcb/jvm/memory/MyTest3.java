package com.gcb.jvm.memory;

/**
 * 测试死锁和工具的使用 jconsole 和 jvisualvm
 */
public class MyTest3 {
    public static void main(String[] args) throws InterruptedException {
        // 这里让AB各开辟一个新的线程
        new Thread(A::method, "Thread A").start();
        new Thread(B::method, "Thread B").start();

        Thread.sleep(40000);
        System.out.println("结束");
     }
}


class A{

    public synchronized static void method(){
        System.out.println("BBB method form B");

        try {
            // 休眠的原因是为了让B拿到B对应的class对象的锁
            Thread.sleep(1000);
            System.out.println("method form B");
            // 这里调用B的synchronized()方法，所以尝试获取它的锁，结果获取失败，因为B早就被
            //另一个线程持有了锁
            B.test();
            B.method();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class B{

    public synchronized static void method(){
        System.out.println("AAA method form A");

        try {
            Thread.sleep(1000);
            System.out.println("method form A");
            A.method();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  static void test(){
        System.out.println("mytest");
    }
}