package com.gcb.jvm.classload;

import java.util.UUID;

/**
 * 测试类的主动使用示例 （得出结论：new数组对象不是主动使用）
 */
public class classloadTest4 {

    public static void main(String[] args) {

        // 这个是主动使用
        //Parent4 parent4 = new Parent4();

        Parent4[] parent4s = new Parent4[1];
        System.out.println(parent4s.getClass());
        /**输出为
         * class [Lcom.gcb.jvm.classload.Parent4;
         * 对于数组实例来说，其类型是由jvm在运行期动态生成的，表示形式如上，对于数组来说，
         * JavaDoc将构成的数组的元素称为Component,实际上就是将数组降低一个维度之后的类型
         *
         * 数组的助记符有两种类型，分为引用类型和基本类型
         *
         * 助记符：anewarray：表示创建一个引用类型（如类、接口）的数组，并将其引用值压入栈顶
         * 助记符：newarray：表示创建一个指定原始类型（int boolean float double）的数组，并将其引用值压入栈顶
         */

        System.out.println(parent4s.getClass().getSuperclass());
        //输出为： class java.lang.Object

        System.out.println("----------------------------");

        int[] ints = new int[1];
        System.out.println(ints.getClass());
        //输出为： class [I

        System.out.println(ints.getClass().getSuperclass());
        //class java.lang.Object

    }
}


class Parent4{

    static {
        System.out.println("parent is loading");
    }
}

