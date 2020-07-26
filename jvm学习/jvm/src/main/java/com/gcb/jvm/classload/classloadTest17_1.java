package com.gcb.jvm.classload;

/**
 * 证明一个类及其类里的所有对象的类都是同一个类加载器加载的
 *
 * 第二个实验证明加载器的命名空间的规律
 *
 */
public class classloadTest17_1 {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        classloadTest16 classloadTest16 = new classloadTest16("loader1");
        classloadTest16.setPath("C:\\\\Users\\\\古春波\\\\Desktop\\\\test");

        Class<?> aClass = classloadTest16.loadClass("com.gcb.jvm.classload.MySample");

        ClassLoader classLoader = aClass.getClassLoader();

        System.out.println(aClass.hashCode());
        //==========================模块一========================
        aClass.newInstance();
        /**
         * 如果将MySample.class,MyCat.class两个文件都删掉，那么两个类都由自定义的
         * classloadTest16加载器加载，但是如果只是将将MyCat.class文件删掉，那么就会
         * 报错，因为MySample.class 是由系统类加载器加载的，而MySample类中new 了一个
         * MyCat的对象，那么Mycat应该也要是系统类加载器加载的（因为如果一个类有类加载器A加载，
         * 那么这个类的依赖类也是由相同的类加载器加载的）,但是由于MyCat.class文件被删掉
         * 显然会报错！
         */

        //==========================模块二========================
        /**
         * 如果删除掉MyCat.class文件,同时在MySample.java类中引用MyCat.class( 即增加代码System.out.println("MySample is loading " +   MyCat.class);)
         * 那么就会报错
         * 因为MySample.java是在系统类加载器中加载的，但是MyCat.class是在自定义的类加载器
         * 中加载的，但是系统类加载器是无法察觉到自定义加载器的加载的类的
         */

    }
}
