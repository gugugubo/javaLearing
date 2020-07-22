package com.concurrent.test2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * 使用volatile设置打断标记，实现两阶段终止
 */
public class Test3 {
    public static void main(String[] args) throws InterruptedException {
        TwoParseTermination twoParseTermination = new TwoParseTermination();
        twoParseTermination.start();
        Thread.sleep(3000);  // 让监控线程执行一会儿
        twoParseTermination.stop(); // 停止监控线程

    }
}


@Slf4j
class TwoParseTermination{
    Thread thread ;
    
    // 设置打断标记为volatile，解决主线程修改对thread线程的可见性问题
    private volatile boolean stop = false;
    
    
    public void start(){

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
        // 这里打断是为了防止如果线程在执行Thread.sleep(500);时要等待一定时间才停止
        thread.interrupt();
    }
}