package com.gcb.jvm.classload;

import java.util.UUID;

/**
 * 测试类的主动使用示例（结论;当一个常量在编译器无法确定时，那么它的值是不会放到常量池中的，显然会导致这个类被初始化）
 */
public class classloadTest3 {

    public static void main(String[] args) {

        System.out.println(Parent3.parentString);

    }
}


class Parent3{

    static final String parentString = UUID.randomUUID().toString();


    static {
        System.out.println("parent is loading");
        /**
         * 输出为
         * parent is loading
         * df4c5ced-c959-4038-8efd-2b5c257ed2a1
         * 此实例作为classloadTest2.java测试的一个补充，当一个常量在编译器无法确定时，
         * 那么它的值是不会放到常量池中的，显然会导致这个类被初始化
         */
    }
}

