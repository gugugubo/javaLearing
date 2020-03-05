package com.gcb.jvm.gc;

public class MyTest1 {

    public static void main(String[] args) {
        int size = 1024 * 1024;

        byte[] myAlloc1 = new byte[2 * size];
        byte[] myAlloc2 = new byte[2 * size];
        byte[] myAlloc3 = new byte[4 * size];

        System.out.println("完成了");
    }
}
