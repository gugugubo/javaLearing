package com.gcb.jvm.classload;

import java.io.*;

/**
 * 自定义类加载器
 * 需要继承ClassLoader类
 * 重写loadClassData 和 findClass方法
 */
public class classloadTest16 extends ClassLoader {

    private String classLoaderName;

    private final String fileExtension = ".class";

    public classloadTest16(String classLoaderName){
        super();  // 调用父类的构造方法，父类的构造方法中设置了类的委托双亲
        this.classLoaderName = classLoaderName;
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

    private byte[] loadClassData(String name ){
        InputStream is = null;
        byte[] data = null;
        ByteArrayOutputStream baos = null;

        try{
            this.classLoaderName = this.classLoaderName.replace(".","/");
            is = new FileInputStream(new File(name + this.fileExtension));
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
        byte[] b = loadClassData(name);
        return this.defineClass(name, b, 0, b.length);
    }

    public static void test(ClassLoader classLoader) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 此方法的底层会调用我自己编写的findClass 和loadClassData 方法
        Class<?> aClass = classLoader.loadClass("com.gcb.jvm.classload.classloadTest1");
        Object instance = aClass.newInstance();

        System.out.println(instance);
        /**
         * 输出结果
         * com.gcb.jvm.classload.classloadTest1@1b6d3586
         */
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        classloadTest16 loader1 = new classloadTest16("loader1");
        test(loader1);
    }
}
