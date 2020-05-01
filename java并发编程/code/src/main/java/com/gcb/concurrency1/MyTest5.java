package com.gcb.concurrency1;

/**
 * 当我们使用synchronized修饰方法时，在方法的字节码中是使用了一个标记进行标识的
 */
public class MyTest5 {
    public static synchronized void method(){
        System.out.println("----test---");
    }
}
