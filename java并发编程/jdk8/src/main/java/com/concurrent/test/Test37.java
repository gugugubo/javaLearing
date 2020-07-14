package com.concurrent.test;

/**
 * 使用notify和wait实现循环打印abcabc
 */
public class Test37 {

    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1, 15);
        new Thread(()->{
            waitNotify.print("a",1,2);
        },"线程一").start();
        new Thread(()->{
            waitNotify.print("b",2,3);
        },"线程二").start();
        new Thread(()->{
            waitNotify.print("c",3,1);
        },"线程三").start();
                
    }
    
    
    
}


/**
 * 设置一个打印标记，比如 
 * 线程输出内容  打印标记   下一个打印标记
 * a            1       2
 * b            2       3
 * c            3       1
 * 每个线程执行打印之前，先检查自己的线程跟标记是否相同，如果相同则打印，并设置下一个打印的标记
 */
class WaitNotify{
    /**
     * fddfdf
     * @param str  要打印的内容
     * @param flag  线程的打印标记
     * @param nextFlag   下一个打印标记
     */
    public void print(String str,int flag, int nextFlag){
        for (int i=0;i<loopNumber;i++){
            synchronized (this){
                while (this.flag != flag){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 标记相同则打印
                System.out.print(str);
                this.flag = nextFlag;
                // 修改了打印标记，唤醒其它线程让他们抢啦！
                this.notifyAll();
            }
        }
    }
    
    int flag ;
    int loopNumber;

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }
}