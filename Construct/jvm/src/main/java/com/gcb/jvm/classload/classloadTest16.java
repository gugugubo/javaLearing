package com.gcb.jvm.classload;

import java.io.*;

/**
 * 自定义类加载器(结论：自定义加载器的如何实现，并且在什么时候会调用自定义加载器)
 * 需要继承ClassLoader类
 * 重写和 findClass方法
 * findclass方法的javadoc文档：
 * 查找具有指定二进制名称的类。此方法应被 遵循用于加载类的委托模型的类装入器 实现重写，
 * 并将在检查父类加载器以获取请求的类后由loadClass方法调用。默认实现引发ClassNotFoundException。
 *
 * findclass类里卖会调用defineClass方法，其文档为：
 * 将字节数组转换为Class类的实例。在Clsss类可以使用之前，必须对其进行解析。
 * 方法返回结果：从指定的类数据创建的Class对象。

 */
public class classloadTest16 extends ClassLoader {

    private String classLoaderName;

    private final String fileExtension = ".class";


    private String path ;

    public void setPath(String path) {
        this.path = path;
    }

    public classloadTest16(String classLoaderName){
        super();  // 调用父类的构造方法，父类的构造方法中设置了类的委托双亲
        this.classLoaderName = classLoaderName;
    }

    public classloadTest16(ClassLoader parent){
        super(parent);  // 调用父类的构造方法，父类的构造方法中设置了类的委托双亲
    }

    public classloadTest16(ClassLoader parent, String classLoaderName){
        super(parent);  // 手动设置委托双亲
        this.classLoaderName = classLoaderName;
    }

    @Override
    public String toString() {
        return "classloadTest16{" +
                "classLoaderName='" + classLoaderName + '\'' +
                '}';
    }

    /**
     * 此方法被findclass方法调用
     * @param name
     * @return
     */
    private byte[] loadClassData(String name ){
        InputStream is = null;
        byte[] data = null;
        ByteArrayOutputStream baos = null;

        try{
            this.classLoaderName = name.replace(".","//");
            String path2 = this.path +"//" + this.classLoaderName + this.fileExtension;
            is = new FileInputStream(new File(path2));
            baos = new ByteArrayOutputStream();

            int ch = 0;
            while (-1!=(ch = is.read())){
                baos.write(ch);
            }
            data  = baos.toByteArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                baos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    @Override
    public Class<?> findClass(String name) {
        // debug 发现 name = com.gcb.jvm.classload.classloadTest1 name是我们loadClass方法中传入的参数
        System.out.println("正在使用自定义的类加载器进行加载类");
        byte[] b = loadClassData(name);
        return this.defineClass(name, b, 0, b.length);
    }



    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
//        // =============================模块一=============================
//        classloadTest16 loader1 = new classloadTest16("loader1");
//        loader1.setPath("C:\\Users\\古春波\\Desktop\\test");
//        // 此方法的底层会调用我自己编写的findClass方法
//        Class<?> aClass = loader1.loadClass("com.gcb.jvm.classload.classloadTest1");
//
//        Object instance = aClass.newInstance();
//
//        System.out.println(instance);
//        System.out.println(aClass.hashCode());
//        System.out.println(aClass.getClassLoader());
//        System.out.println("------------------------");
//        classloadTest16 loader2 = new classloadTest16("loader2");
//        loader2.setPath("C:\\Users\\古春波\\Desktop\\test");
//        // 此方法的底层会调用我自己编写的findClass方法
//        Class<?> aClass2 = loader2.loadClass("com.gcb.jvm.classload.classloadTest1");
//        Object instance2 = aClass.newInstance();
//
//        System.out.println(aClass2.hashCode());
//        System.out.println(instance2);
//        System.out.println(aClass2.getClassLoader());
        /**
         * 以上代码的输出结果为
         * com.gcb.jvm.classload.classloadTest1@1b6d3586
         * 1163157884
         * sun.misc.Launcher$AppClassLoader@18b4aac2
         * ------------------------
         * 1163157884
         * com.gcb.jvm.classload.classloadTest1@74a14482
         * sun.misc.Launcher$AppClassLoader@18b4aac2
         *
         * 因为class文件处于系统加载器所能加载的目录下，系统加载器会根据提供的name = com.gcb.jvm.classload.classloadTest1从目录下查找
         * 所以此类是由系统加载器加载的,并且由于类加载器
         * 只加载一次，所以第二次加载实际上是不会再加载的，所以获得的aClass2对象的hashCode和aClass的是一样的
         */

        /**
         * 删除掉classes目录下的 classloadTest2.class 文件，然后再执行的结果如下
         * om.gcb.jvm.classload.classloadTest1@677327b6
         * 21685669
         * classloadTest16{classLoaderName='com//gcb//jvm//classload//classloadTest1'}
         * ------------------------
         * 正在使用自定义的类加载器进行加载类
         * 325040804
         * com.gcb.jvm.classload.classloadTest1@45ee12a7
         * classloadTest16{classLoaderName='com//gcb//jvm//classload//classloadTest1'}
         * 说明了是使用了自己定义的类加载器进行加载的,，并且由于命名空间的原因，两次加载的Class对象是不一样的
         */

        // =============================模块二=============================
        classloadTest16 loader1 = new classloadTest16("loader1");
        loader1.setPath("C:\\Users\\古春波\\Desktop\\test");
        // 此方法的底层会调用我自己编写的findClass方法
        Class<?> aClass = loader1.loadClass("com.gcb.jvm.classload.classloadTest1");

        Object instance = aClass.newInstance();

        System.out.println(instance);
        System.out.println(aClass.hashCode());
        System.out.println(aClass.getClassLoader());

        System.out.println("------------------------");
        classloadTest16 loader2 = new classloadTest16(loader1,"loader2");
        loader2.setPath("C:\\Users\\古春波\\Desktop\\test");
        // 此方法的底层会调用我自己编写的findClass方法
        Class<?> aClass2 = loader2.loadClass("com.gcb.jvm.classload.classloadTest1");
        Object instance2 = aClass.newInstance();

        System.out.println(aClass2.hashCode());
        System.out.println(instance2);
        System.out.println(aClass2.getClassLoader());

        System.out.println("------------------------");
        classloadTest16 loader3 = new classloadTest16("loader3");
        loader3.setPath("C:\\Users\\古春波\\Desktop\\test");
        // 此方法的底层会调用我自己编写的findClass方法
        Class<?> aClass3 = loader3.loadClass("com.gcb.jvm.classload.classloadTest1");
        Object instance3 = aClass.newInstance();

        System.out.println(aClass3.hashCode());
        System.out.println(instance3);
        System.out.println(aClass3.getClassLoader());
        /**
         * 删除掉 classloadTest1.class 文件之后的输出结果是这样的
         * 正在使用自定义的类加载器进行加载类
         * com.gcb.jvm.classload.classloadTest1@74a14482
         * 356573597
         * classloadTest16{classLoaderName='com//gcb//jvm//classload//classloadTest1'}
         * ------------------------
         * 356573597
         * com.gcb.jvm.classload.classloadTest1@677327b6
         * classloadTest16{classLoaderName='com//gcb//jvm//classload//classloadTest1'}
         * ------------------------
         * 正在使用自定义的类加载器进行加载类
         * 1836019240
         * com.gcb.jvm.classload.classloadTest1@135fbaa4
         * classloadTest16{classLoaderName='com//gcb//jvm//classload//classloadTest1'}
         *
         * loader1 和 loader2 加载的Class对象是相同的，因为 loader2 的父加载器
         * 被定义为loader1，由于loader已经加载过 classloadTest1.class，所以loader2无需再次加载
         * 然后由于命名空间的存在，loader3是不知道loader2已经加载过classloadTest1.class的，所以它会
         * 重新加载
         *
         */
    }
}
