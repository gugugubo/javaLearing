package com.gcb.jvm.bytecodetest;


/**
 * cd 到classes 目录下 javap -verbose com.gcb.jvm.bytecodetest.MyTest1
 * 可以得到16进制字节码翻译过来的信息
 */
public class MyTest1 {

    private int a = 1;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public static void main(String[] args) {
        String test = "javalanguagespecification";
        String str = "java";
        String str2 = "language";
        String str3 = "specification";
        System.out.println(test == str + str2 + str3);
        System.out.println(test == "java" + "language" + "specification");
    }
//public static void main(String[] args) {
//    String s1 = "hello";
//    String s2 = "hello";
//    String s3 = "he" + "llo";
//    String s4 = "hel" + new String("lo");
//    String s5 = new String("hello");
//    String s6 = s5.intern();
//    String s7 = "h";
//    String s8 = "ello";
//    String s9 = s7 + s8;
//    System.out.println(s1==s2);//true
//    System.out.println(s1==s3);//true
//    System.out.println(s1==s4);//false
//    System.out.println(s1==s9);//false
//    System.out.println(s4==s5);//false
//    System.out.println(s1==s6);//true
//}

}
