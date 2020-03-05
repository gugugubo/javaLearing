package com.gcb.jvm.classload;

/**
 * 测试类加载器的双亲委托机制，（结论：和预期得一样）
 */
public class classloadTest14 {
    public static void main(String[] args){
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        System.out.println(systemClassLoader);

        while (null!= systemClassLoader){
            systemClassLoader = systemClassLoader.getParent();
            System.out.println(systemClassLoader);
        }
    }
}
