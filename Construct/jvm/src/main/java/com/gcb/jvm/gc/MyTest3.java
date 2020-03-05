package com.gcb.jvm.gc;

public class MyTest3 {

//    public static void main(String[] args) {
//        int size = 1024 * 1024;
//        byte[] myAlloc1 = new byte[2 * size];
//        byte[] myAlloc2 = new byte[2 * size];
//        byte[] myAlloc3 = new byte[2 * size];
//        byte[] myAlloc4 = new byte[2 * size];
//        System.out.println("完成了");
//    }

    public static void main(String[] args) {
        String s1 = new String("hello");
        String intern1 = s1.intern();
        String s2 = "hello";
        System.out.println(s1 == s2);
        String s3 = new String("hello") + new String("hello");
        String intern3 = s3.intern();
        String s4 = "hellohello";
        System.out.println(s3 == s4);
    }
    /**
     * 　　在jdk1.6下运行的结果为：
     * 　　false,false
     * 　　在jdk1.7,1.8下运行的结果为：
     * 　　false,true
     */
}
