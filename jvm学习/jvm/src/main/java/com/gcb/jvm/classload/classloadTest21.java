package com.gcb.jvm.classload;

import java.lang.reflect.Method;

/**
 * 验证类的命名空间（结论：不同命名空间中的类是无法再一起使用的）
 */
public class classloadTest21 {

    public static void main(String[] args) throws Exception{
        classloadTest16 classloadTest16 = new classloadTest16("loader1");
        classloadTest16.setPath("C:\\\\Users\\\\古春波\\\\Desktop\\\\test");
        classloadTest16  classloadTest16_2= new classloadTest16("loader2");
        classloadTest16_2.setPath("C:\\\\Users\\\\古春波\\\\Desktop\\\\test");
        Class<?> aClass = classloadTest16.loadClass("com.gcb.jvm.classload.Person");

        Class<?> aClass1 = classloadTest16_2.loadClass("com.gcb.jvm.classload.Person");

        Object instance = aClass.newInstance();

        Object instance1 = aClass1.newInstance();

        System.out.println(aClass.hashCode());
        System.out.println(aClass1.hashCode());

        // 获取对象的setPerson方法,Object.class是的setPerson参数类型
        Method setPerson = aClass.getMethod("setPerson",Object.class);
        // 第一个参数是执行此方法的对象，第二个参数是传递给方法的参数
        setPerson.invoke(instance,instance1);
        /**
         * 将Person.class文件删除，再执行，输出结果如下
         * 正在使用自定义的类加载器进行加载类
         * 正在使用自定义的类加载器进行加载类
         * 21685669
         * 2133927002
         * Exception in thread "main" java.lang.reflect.InvocationTargetException
         * 	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
         * 	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
         * 	不能将不同命名空间中加载的类混在一起使用
         */
    }
}
