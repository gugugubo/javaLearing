package com.gcb.jvm.classload;


/**
 * 接口和类初始化的区别
 * 接口初始化和类初始化的区别  唯一一种情况
 *当一个接口在初始化时，并不要求其父接口都完成了初始化
 * 只有在真正使用到父接口的时候（如引用接口中定义的常量），才会初始化
 */
public class classloadTest5 {
    public static void main(String[] arg){
        System.out.println(Chirld5.b);
        /**
         * 输出结果为只 6
         * 可以证明一个父接口并不会因为它的实现类的初始化而初始化
         *
         */

        System.out.println(test2.thread);
        /**
         * 输出结果为仅为 Parent is loading
         * 可以证明一个父接口并不会因为它的子接口的初始化而初始化
         */
    }
}


interface Parent5{
    public static final int c = 6;

    /**
     * 当Parent5被实例化时，此代码就会执行
     */
    public static Thread thread = new Thread(){
        {
            System.out.println("Parent5 is loading");
        }
    };
}

class Chirld5 implements Parent5{
    public static  int b = 6;
}


interface test1{
    /**
     * 当Parent5被实例化时，此代码就会执行
     */
    public static Thread thread = new Thread(){
        {
            System.out.println("test1 is loading");
        }
    };
}

interface  test2 extends  test1{

    /**
     * 当Parent5被实例化时，此代码就会执行
     */
    public static Thread thread = new Thread(){
        {
            System.out.println("test2 is loading");
        }
    };
}


