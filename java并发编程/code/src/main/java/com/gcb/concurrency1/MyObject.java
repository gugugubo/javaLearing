package com.gcb.concurrency1;

/**
 * 编写多线程程序，实现下列目标
 *
 * 1. 存在一个对象，该对象有一个int类型的成员变量counter，该成员变量的初始值为0
 * 2. 创建两个线程，其中一个线程对该对象的成员变量counter增1，另一个线程减1
 * 3. 输出该对象成员变量counter每次变化之后的值
 * 4. 最终输出结果应为0101010101......
 */
public class MyObject {

    private int counter;

    public synchronized void increase(){
        if (counter!=0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        counter++;
        System.out.println(counter);
        notify();
    }

    public synchronized void decrease(){
        if (counter==0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        counter--;
        System.out.println(counter);
        notify();
    }

    /**
     * 拓展：如果说创建多个线程对同一对象的成员变量进行更改，那么上述程序可能会出现问题
     * 创建两个increaseThread线程和两个decreaseThread线程，那么就会出现问题
     *    public static void main(String[] args) {
     *         MyObject myObject = new MyObject();
     *         DecreaseThread decreaseThread = new DecreaseThread(myObject);
     *         IncreaseThread increaseThread = new IncreaseThread(myObject);
     *         new DecreaseThread(myObject).start();
     *         new IncreaseThread(myObject).start();
     *         increaseThread.start();
     *         decreaseThread.start();
     *     }
     * 比如最开始创建两个减少的线程都先后执行，然后都会wait
     * 然后一个增加线程执行
     * 执行完增加线程后又先后唤醒两个减少线程，因为唤醒的线程从wait方法出继续执行的，所以会出现为题
     * 可以作如下的修改，将if改为while
     *     public synchronized void increase(){
     *         while (counter!=0){
     *             try {
     *                 wait();
     *             } catch (InterruptedException e) {
     *                 e.printStackTrace();
     *             }
     *         }
     *         counter++;
     *         System.out.println(counter);
     *         notify();
     *     }
     *
     *     public synchronized void decrease(){
     *         while (counter==0){
     *             try {
     *                 wait();
     *             } catch (InterruptedException e) {
     *                 e.printStackTrace();
     *             }
     *         }
     *         counter--;
     *         System.out.println(counter);
     *         notify();
     *     }
     *
     */
}
