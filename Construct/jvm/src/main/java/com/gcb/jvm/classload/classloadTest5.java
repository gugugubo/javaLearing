package com.gcb.jvm.classload;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 接口初始化和类初始化的区别  唯一一种情况
 *当一个接口在初始化时，并不要求其父接口都完成了初始化
 * 只有在真正使用到父接口的时候（如引用接口中定义的常量），才会初始化
 */
public class classloadTest5 {
    public static void main(String[] arg){
        System.out.println(Chirld5.b);
    }
}


interface Parent5{
    public static final int c = 6;
}

interface Chirld5 extends Parent5{
    public static final int b = 6;
    //public static final int b = new Random().nextInt(4);
}

