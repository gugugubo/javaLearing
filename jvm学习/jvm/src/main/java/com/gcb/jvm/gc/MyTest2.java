package com.gcb.jvm.gc;

public class MyTest2 {

    public static void main(String[] args) throws InterruptedException {
        int size = 1024 * 1024;

        byte[] myAlloc1 = new byte[5 * size];

        Thread.sleep(1000000);
    }
}
