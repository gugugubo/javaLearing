package com.gcb.jvm.classload;

/**
 * 查看数组得类加载器（结论：如果数组得元素类型是引用类型，则类加载器是系统加载器，如果是基本类型，则为null）
 */
public class classloadTest15 {


    public static void main(String[] args){
        String [] strings = new String[1];
        ClassLoader classLoader = strings.getClass().getClassLoader();
        System.out.println(classLoader);

        classloadTest15 [] classloadTest15s = new classloadTest15[1];
        ClassLoader classLoader1 = classloadTest15s.getClass().getClassLoader();
        System.out.println(classLoader1);


        Integer [] integers = new Integer[1];
        ClassLoader classLoader2 = integers.getClass().getClassLoader();
        System.out.println(classLoader2);
        /**
         * 输出结果
         * null
         * sun.misc.Launcher$AppClassLoader@18b4aac2
         * null
         */
    }
}
