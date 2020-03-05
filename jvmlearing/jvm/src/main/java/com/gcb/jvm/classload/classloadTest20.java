package com.gcb.jvm.classload;

import java.lang.reflect.Method;

/**
 * 简单的测试模型
 */
public class classloadTest20 {

    public static void main(String[] args) throws Exception{
        classloadTest16 classloadTest16 = new classloadTest16("loader1");
        classloadTest16 classloadTest16_2 = new classloadTest16("loader2");

        Class<?> aClass = classloadTest16.loadClass("com.gcb.jvm.classload.Person");

        Class<?> aClass1 = classloadTest16_2.loadClass("com.gcb.jvm.classload.Person");

        Object instance = aClass.newInstance();

        Object instance1 = aClass1.newInstance();

        // 获取对象的setPerson方法,Object.class是setPerson的参数类型
        Method setPerson = aClass.getMethod("setPerson",Object.class);
        // 第一个参数是执行此方法的对象，第二个参数是传递给方法的参数
        setPerson.invoke(instance,instance1);

    }
}
