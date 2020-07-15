package com.concurrent.test2;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Test14 {
    static Unsafe unsafe;
    static {
        try {
            // Unsafe 使用了单例模式，unsafe对象是类中的一个私有的变量
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }



    
    
    static Unsafe getUnsafe() {
        return unsafe;
    }

    
}




