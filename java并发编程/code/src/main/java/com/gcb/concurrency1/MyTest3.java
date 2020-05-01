package com.gcb.concurrency1;

/**
 * interrupt方法的使用
 */
public class MyTest3 {

    public static void main(String[] args) {
        Thread threadOne = new Thread(new Runnable() {
            public void run() {
                System.out.println("--begin111--");
                while (true){

                }
            }
        });

        final Thread mainThread = Thread.currentThread();
        Thread threadTwo = new Thread(new Runnable() {
            public void run() {
                System.out.println("--begin222--");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mainThread.interrupt();
                System.out.println("--end222--");
            }
        });

        threadOne.start();
        threadTwo.start();

        try {
            threadOne.join();
        } catch (InterruptedException e) {
            System.out.println("main thread " + e);
        }
    }
}
