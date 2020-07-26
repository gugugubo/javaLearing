package com.gcb.jvm.classload;

import sun.misc.Launcher;

/**
 * 结论一实验：cd 到classes文件目录下，执行命令java -Dsun.boot.class.path=./ com.gcb.jvm.classload.classload.classloadTest23
 * 提示：
 *Error occurred during initialization of VM
 * java/lang/NoClassDefFoundError: java/lang/Object
 * 结论一：如果将sun.boot.class.paht参数改为其它目录，
 * 那么将无法加载Object类，然而java所有的类都依赖于Object类，就会导致加载失败。
 *
 * 结论二，启动类加载器不是一个java类，而是jvm内建代码块，用于加载其它类加载器
 *
 * 结论三： 指定java.system.class.loader系统属性可以修改系统类加载器
 */
public class classloadTest23 {
    public static void main(String[] args) {
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("java.class.path"));
        /**
         * 那么类加载器是谁加载的呢？内建于java平台中的启动类加载器会加载java.lang.classloader类
         * 和其他的java平台类(即jre正常运行所需的基本组件，包括java.util 和java.lang包等等)。
         * 当jvm启动时，一块特殊的C++机器码会执行，它会加载拓展类加载器与系统类加载器，
         * 这块特殊的机器码叫做启动类加载器。启动类加载器不是java类，而其他的类加载器是java类。
         */
        System.out.println(ClassLoader.class.getClassLoader());

        /**
         * 输出
         * null
         * 说明ClassLoader类是由启动类加载器加载的,结论二
         */

        /**
         * 因为系统类加载器和拓展类加载器是Launcher类的内部类，我们无法直接获取它的内部类，但是可以观察
         * Launcher类的类加载器就知道系统类加载器和拓展类加载器是由谁加载的
         */
        System.out.println(Launcher.class.getClassLoader());
        /**
         * 输出
         * null
         * 结论得到验证
         * 结论二
         */

        /**
         * 在classloadTest16中添加一个构造方法，构造函数接受一个Classloader 类型的参数,此Classloader 作为此类的父加载器。
         * cd 到classes文件目录下，执行命令
         * java -Djava.system.class.loader=com.gcb.jvm.classload.classloadTest16 com.gcb.jvm.classload.classloadTest23
         */
        System.out.println(ClassLoader.getSystemClassLoader());
        /**
         * 输出为
         * classloadTest16{classLoaderName='null'}，说明我们成功制定了系统类加载器
         * 结论三
         */

    }
}
