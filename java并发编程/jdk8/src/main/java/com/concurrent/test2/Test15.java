package com.concurrent.test2;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Test15 {
    public static void main(String[] args) throws NoSuchFieldException {

        Unsafe unsafe = Test14.getUnsafe();
        Field id = Student.class.getDeclaredField("id");
        Field name = Student.class.getDeclaredField("name");
        // 1.获得成员变量的偏移量
        long idOffset = Test14.unsafe.objectFieldOffset(id);
        long nameOffset = Test14.unsafe.objectFieldOffset(name);
        Student student = new Student();
        // 2.使用 cas 方法替换成员变量的值
        Test14.unsafe.compareAndSwapInt(student, idOffset, 0, 20); // 返回 true
        Test14.unsafe.compareAndSwapObject(student, nameOffset, null, "张三"); // 返回 true
        
        //3.验证
        System.out.println(student);
    }
}


class Student {
    volatile int id;
    volatile String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
