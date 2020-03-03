package com.gcb.jvm.memory;

/**
 * 测试StackOverflowError
 * -Xss160k 设置栈的最大值
 */
public class MyTest2 {

    private int length;

    public int getLength(){
        return length;
    }

    public void test(){
        this.length ++;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test();
    }

    public static void main(String[] args) {
        MyTest2 myTest2 = new MyTest2();
        try{
            myTest2.test();
        }catch (Throwable e){
            System.out.println(myTest2.getLength());
            e.printStackTrace();
        }
    }
}
