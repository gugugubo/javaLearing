package com.concurrent.test2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * 使用volatile设置打断标记，实现两阶段终止，并且设置只可以启动一次,即Balking 模式
 * 可以使用double-checked locking 模式进行过加强
 */
public class Test4 {
    public static void main(String[] args) throws InterruptedException {
        TwoParseTermination2 twoParseTermination = new TwoParseTermination2();
        twoParseTermination.start();
        twoParseTermination.start();
        Thread.sleep(3000);  // 让监控线程执行一会儿
        twoParseTermination.stop(); // 停止监控线程
    }
    
    
}


@Slf4j
class TwoParseTermination2{
    Thread thread ;
    
    // 设置打断标记为volatile，解决可见性问题
    private volatile boolean stop = false;
    
    private boolean starting = false;
    
    public void start(){
        // 如果不加锁，多个线程同时进入if进行判断的话就会出问题，所以要加锁！synchronized也保证了可见性，所以这里的starting没有使用volatile
        synchronized (this){
            if (starting){
                return;
            }
            starting = true;
        }
        
        thread = new Thread(()->{
            while(true){
                if (stop){
                    log.debug("线程结束。。正在料理后事中");
                    break;
                }
                try {
                    Thread.sleep(500);
                    log.debug("正在执行监控的功能");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    
    public void stop(){
        this.stop = true;
        // 这里打断是为了防止如果线程在执行Thread.sleep(500);时，要等待一定时间才停止
        thread.interrupt();
    }

}



