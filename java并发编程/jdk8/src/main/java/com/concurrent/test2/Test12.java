package com.concurrent.test2;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class Test12 {

    private volatile int field;
    public static void main(String[] args) {
        AtomicReferenceFieldUpdater fieldUpdater =
                AtomicReferenceFieldUpdater.newUpdater(Testx.class, String.class,"field");
        Testx testx = new Testx();
        fieldUpdater.compareAndSet(testx, null, "李华");
        // 修改成功 field = "李华"
        System.out.println(testx.field);
        // 修改失败 field = "李华"
        fieldUpdater.compareAndSet(testx, null, "zhang华");
        System.out.println(testx.field);



    }
}

class Testx{
    volatile String field;

    @Override
    public String toString() {
        return "Testx{" +
                "field='" + field + '\'' +
                '}';
    }
}


