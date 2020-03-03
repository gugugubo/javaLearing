package com.gcb.jvm.g1;

public class MyTest1 {
    public static void main(String[] args) {
        int size = 1024 * 1024;
        byte[] myAlloc1 = new byte[size];
        byte[] myAlloc2 = new byte[size];
        byte[] myAlloc3 = new byte[size];
        byte[] myAlloc4 = new byte[size];
        System.out.println("完成了");
    }
}
