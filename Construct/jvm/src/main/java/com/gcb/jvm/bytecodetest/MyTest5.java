package com.gcb.jvm.bytecodetest;


/**
 *
 *
 * Grandpa g1 = new Father();
 * 以上代码，变量g1 的静态类型是 Grandpa ，而变量的实际类型（真正指向的类型）是Father
 * 我们可以得出这样一个结论：变量的静态类型是不会发生变化的，而变量的实际类型是
 * 可以发生变化的（多态的一种体现），实际类型是在运行期才可以确定。
 *
 *
 * 以下实验验证类静态分派机制
 */
public class MyTest5 {

    public void test(Grandpa grandpa){
        System.out.println("grandpa");
    }

    public void test(Father father){
        System.out.println("Father");
    }

    public void test(Son son){
        System.out.println("Son");
    }

    public static void main(String[] args) {
        Grandpa g1 = new Father();
        Grandpa g2 = new Son();

        MyTest5 myTest5 = new MyTest5();

        myTest5.test(g1);
        myTest5.test(g2);
        /**
         * 输出结果为：
         * grandpa
         * grandpa
         * 这是因为方法的重载，是一种静态行为，即重载方法只关系传递的g1 和 g2 的静态类型，而不关心它的实际类型
         * 以上称为方法的静态分派：
         * 在字节码中，就是显示接受的静态类型
         */
    }
}




class Grandpa{

}

class Father extends  Grandpa{

}

class Son extends Father{}