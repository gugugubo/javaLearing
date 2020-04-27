package com.gcb.concurrency1;

public class MyTest2 {
    static Object obj= new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread =  new Thread(new Runnable() {
            public void run() {
                System.out.println("--begin111--");
                synchronized (obj){
                    try {
                        wait();
                        System.out.println("end1111----");
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
        Thread.sleep(1000);
        System.out.println("--begin2222--");
        thread.interrupt();
        Thread.sleep(1000);
        System.out.println("end2222----");
    }

    /**输出
     * --begin111--
     * Exception in thread "Thread-0" java.lang.IllegalMonitorStateException
     * 	at java.lang.Object.wait(Native Method)
     * 	at java.lang.Object.wait(Object.java:502)
     * 	at com.gcb.concurrency1.MyTest2$1.run(MyTest2.java:12)
     * 	at java.lang.Thread.run(Thread.java:748)
     * --begin2222--
     * end2222----
     */
}
