package com.gcb.jvm.classload;

import com.sun.crypto.provider.AESKeyGenerator;


/**
 * 验证拓展类加载器的加载目录是根据系统变量所定义的来加载的，如果修改该系统变量，那么就会无法正常加载
 * cd 到classes文件目录下，执行命令java -Djava.ext.dirs=./ com.gcb.jvm.classload.classload.classloadTest19
 */
public class classloadTest19 {
    public static void main(String[] args) {
        System.out.println("testing");
        //该类默认有扩展类加载器加载的,但是如果我们把该类默认的加载路径修改后，就会报错
        AESKeyGenerator aesKeyGenerator = new AESKeyGenerator();
        System.out.println(aesKeyGenerator.getClass().getClassLoader()); //sun.misc.Launcher$ExtClassLoader@4b67cf4d
    }
}
