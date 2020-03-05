package com.gcb.jvm.memory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 模拟堆溢出OutOfMemoryError 异常
 *  虚拟机参数,堆的最小和最大空间都是5m
 * -XX:+HeapDumpOnOutOfMemoryError 将错误信息文件输出
 * -Xms5m -Xmx5m -XX:+HeapDumpOnOutOfMemoryError
 */
public class MyTest1 implements Serializable {



    public static void main(String[] args) {
        List<MyTest1> list = new ArrayList();
        for (;;){
            list.add(new MyTest1());
//            System.gc();
        }
    }
}
