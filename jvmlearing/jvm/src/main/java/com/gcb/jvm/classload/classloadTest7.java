package com.gcb.jvm.classload;

/**
 *  验证类加载器的种类
 */
public class classloadTest7 {
    public static void  main(String[] arg) throws ClassNotFoundException {
        Class<?> aClass = Class.forName("java.lang.String");
        System.out.println(aClass.getClassLoader());
        /**输出
         * null
         * 代表是由启动类加载器加载的
         *
         */

        Class<?> bClass = Class.forName("com.gcb.jvm.classload.C");
        System.out.println(bClass.getClassLoader());
        /**输出
         * sun.misc.Launcher$AppClassLoader@18b4aac2
         * 代表是由系统类加载器 加载的
         *
         */
    }

}

class C{

}
