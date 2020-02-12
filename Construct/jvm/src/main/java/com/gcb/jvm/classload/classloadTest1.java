package com.gcb.jvm.classload;


/**
 * 测试类的主动使用示例
 */

/**
 *  虚拟机参数
 *  --XX:+TraceClassloading: 用于追踪类的加载信息
 *  <h2>jvm参数设置规律</h2>
 * <P> -XX:+<option>，表示开启option选项</p>
 * <P>-XX:-<option>，表示关闭option选项</p>
 * <P>-XX:<option>=value，表示将option的值设置为value</p>
 */
public class classloadTest1 {

    public static void main(String[] args) {
        short a = 3;
        // System.out.println(Chirld.parentString);
        /**
         * 这是因为类的初始化仅在被主动使用时才进行初始化,只有用Chirld.parentString只是主动使用了Parent类
         * parent is loading
         * parent
         */
        System.out.println("-------------'");

        System.out.println(Chirld.chirldString);
        /**
         * 这里会主动使用父类！所以父类也被初始化
         * parent is loading
         * chirld is loading
         * chirld
         */
    }
}


class Parent{

    static String parentString = "parent";

    static {
        System.out.println("parent is loading");
    }
}

class Chirld extends  Parent{

    static String chirldString = "chirld";

    static {
        System.out.println("chirld is loading");
    }
}