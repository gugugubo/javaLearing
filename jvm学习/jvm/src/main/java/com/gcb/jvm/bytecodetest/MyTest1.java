package com.gcb.jvm.bytecodetest;


/**
 * cd 到classes 目录下 javap -verbose com.gcb.jvm.bytecodetest.MyTest1
 * 可以得到16进制字节码翻译过来的信息
 */
public class MyTest1 {

    private int a = 2;

    public static long b = 2L;
    public float e = (float) 1.1;

    public final MyTest1 test = new MyTest1();

    public String c = "d";

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public static void main(String[] args) {

        // 在堆中创建字符串对象”Java“
// 将字符串对象”Java“的引用保存在字符串常量池中
        String s1 = "Java";
// 直接返回字符串常量池中字符串对象”Java“对应的引用
        String s2 = s1.intern();
// 会在堆中在单独创建一个字符串对象
        String s3 = new String("Java");
// 直接返回字符串常量池中字符串对象”Java“对应的引用
        String s4 = s3.intern();
// s1 和 s2 指向的是堆中的同一个对象
        System.out.println(s1 == s2); // true
// s3 和 s4 指向的是堆中不同的对象
        System.out.println(s3 == s4); // false
// s1 和 s4 指向的是堆中的同一个对象
        System.out.println(s1 == s4); //true

        String s5 = new String("Javaf");
        System.out.println("Javaf" == s5.intern()); //true

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
