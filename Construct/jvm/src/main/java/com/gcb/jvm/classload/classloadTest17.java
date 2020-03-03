package com.gcb.jvm.classload;

/**
 * 此类仅为简单示例
 */
public class classloadTest17 {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        classloadTest16 classloadTest16 = new classloadTest16("loader1");

        Class<?> aClass = classloadTest16.loadClass("com.gcb.jvm.classload.MySample");

        ClassLoader classLoader = aClass.getClassLoader();

        System.out.println(aClass.hashCode());
        aClass.newInstance();
        /**
         * 输出
         * 1163157884
         * MySample is loading sun.misc.Launcher$AppClassLoader@18b4aac2
         * MyCat is loading sun.misc.Launcher$AppClassLoader@18b4aac2
         *
         * 因为都是使用系统类加载器加载的
         */


    }
}
