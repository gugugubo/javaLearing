package com.gcb.jvm.classload;


/**
 * 测试类的主动使用示例,发现引用另一个类的常量不会对那个类算是主动使用
 */
public class classloadTest2 {

    public static void main(String[] args) {

        classloadTest1.say();

        System.out.println("-------------'");

        System.out.println(Parent2.parentString);
        /**
         * 输出结果为parent，因为常量在编译阶段会保存在调用这个常量的方法的所在类的常量池当中，
         *本质上，调用类并没有直接调用到定义常量的类，因此并不会触发定义常量的类的初始化
         * 注意，在本示例代码中指的是将常量放到了classloadTest2的常量池中，之后classloadTest2就和Parent2
         * 没有任何关系了，甚至可以将Parent2.class文件删除
         */

        /**
         * 以下测试常量在编译过后的是怎么样的，对class文件进行反编译
         * 可以得到一些助记符标记的一些值，以下是一些助记符
         *常见的注记符
         * 助记符 ldc：表示将int、float或者String类型的常量值从常量池中推送至栈顶
         * 助记符 bipush：表示将单字节（-128-127）的常量值推送到栈顶
         * 助记符 sipush：表示将一个短整型值（-32768-32369）推送至栈顶
         * 助记符 iconst_1：表示将int型的1推送至栈顶（iconst_m1到iconst_5）
         */
        System.out.println(Parent2.aShort);
    }
}


class Parent2{

    static final String parentString = "parent";

    static final short aShort = 127;

    static final short aShort2 = 127;

    static final int anInt = 128;

    static final int big = 6;


    static {
        System.out.println("parent is loading");
    }
}

