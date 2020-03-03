package com.gcb.jvm.classload;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * 测试类的得到几种加载器（结论如下）
 - 1.当前上下文的classloader
 ClassLoader contextClassLoader  = Thread.currentThread().getContextClassLoader();
 - 2.获取当前类的classloader
 Class<classloadTest13> classloadTest13Class = classloadTest13.class;
 ClassLoader classLoader = classloadTest13Class.getClassLoader();
 - 3.获取系统的classloader
 ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
 */
public class classloadTest13 {
    public static void main(String[] args) throws IOException {
        /**
         * getContextClassLoader 的javadoc文档如下：
         上下文类加载器由线程的创建者提供，供在此线程中运行的代码加载类和资源时使用。如果未设置，
         则默认为父线程的上下文类加载器。
         原始线程的上下文类加载器通常设置为用于加载应用程序的类加载器(即system classloader)
         */
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        String resourceName = "com/gcb/jvm/classload/classloadTest13.class";
        Enumeration<URL> urls;
        urls = contextClassLoader.getResources(resourceName);
        while (urls.hasMoreElements()){
            System.out.println(urls.nextElement() + "-");
        }
        /**
         * 输出结果为
         * file:/C:/Users/%e5%8f%a4%e6%98%a5%e6%b3%a2/Desktop/JavaLearing/Construct/jvm/target/classes/com/gcb/jvm/classload/classloadTest13.class-
         */


        System.out.println("--------------");

        Class<classloadTest13> classloadTest13Class = classloadTest13.class;
        ClassLoader classLoader = classloadTest13Class.getClassLoader();
        System.out.println(classLoader);
        /**
         * 输出结果为
         * sun.misc.Launcher$AppClassLoader@18b4aac2
         */

    }
}
