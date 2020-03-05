package com.gcb.jvm.gc;

public class MyTest4 {

    public static void main(String[] args) throws InterruptedException {
        byte[] bytes1 = new byte[1024 * 1024];
        byte[] bytes2 = new byte[1024 * 1024];

        method();
        Thread.sleep(20000);
        System.out.println("11111111");

        method();
        Thread.sleep(4000);
        System.out.println("222222222");

        method();
        Thread.sleep(4000);
        System.out.println("3333333333");

        method();
        Thread.sleep(4000);
        System.out.println("4444444444");

        byte[] bytes3 = new byte[1024 * 1024];
        byte[] bytes4 = new byte[1024 * 1024];
        byte[] bytes5 = new byte[1024 * 1024];

        method();
        Thread.sleep(4000);
        System.out.println("555555");

        method();
        Thread.sleep(1000);
        System.out.println("666666");
    }

    public static void method() {
        for (int i = 0; i < 40; i++) {
            byte[] bytes = new byte[1024 * 1024];
        }
    }
}
