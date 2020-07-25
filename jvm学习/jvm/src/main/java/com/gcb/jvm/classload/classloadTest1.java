package com.gcb.jvm.classload;


/**
 * 测试类的主动使用示例(结论：主动使用的时候类才会被初始化)
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

    protected static void say(){
        System.out.println(232323);
    }

    public static void main(String[] args) {
        short a = 3;
        // System.out.println(Chirld.parentString);
        /**
         * 这是因为类的初始化仅在被主动使用时才进行初始化,只用Chirld.parentString只是主动使用了Parent类
         * 仅运行上面一条语句输出如下：
         * parent is loading
         * parent
         */


        System.out.println(Chirld.chirldString);
        /**
         * 仅运行上面一条语句
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