package com.gcb.jvm.bytecodetest;


/**
 * 方法的动态分派
 *  方法的动态分派涉及到一个重要的概念：
 *  方法接受者（即方法的调用者）
 *查找流程的调用过程是这样的，找到操作数栈顶元素的实际类型，如果在常量池的这个实际类型中找到了
 * 方法描述符和方法名称都和要调用的方法完全相同的方法，并且具响应的访问权限。那么就返回这个方法
 * 的直接引用，如果没有找到，则沿着继承体系从下往上查找 。
 *
 *     比较方法重载（overload）和方法重写（overwrite），我们可以得出这样的结论：
 *     方法重载是静态的，是编译器行为；方法重写是动态的，是运行期行为。
 */
public class MyTest6 {



    public static void main(String[] args) {
        Fruit apple = new Apple();
        Fruit orange = new Orange();
        //在常量池的Apple这个类中查找和Fruit.test方法名称和描述符相同的方法，结果找到了，那就返回apple类的test
        //方法的直接引用
        apple.test();
        orange.test();

        apple = new Orange();
        apple.test();
    }
}


class Fruit{
    public void test(){
        System.out.println("Fruit");
    }
}

class Apple extends Fruit{
    @Override
    public void test(){
        System.out.println("Apple");
    }
}

class Orange extends Fruit{
    @Override
    public void test(){
        System.out.println("Orange");
    }
}