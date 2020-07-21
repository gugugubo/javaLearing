package com.concurrent.test2;

import lombok.extern.slf4j.Slf4j;

import java.sql.Time;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 步骤4：自定义线程池测试
 */
@Slf4j(topic = "Test18")
public class Test18{
    public static void main(String[] args) {
            ThreadPool threadPool = new ThreadPool(1,
                    1000, TimeUnit.MILLISECONDS, 1, (queue, task)->{
                // 1. 死等
                // queue.put(task);
                // 2) 带超时等待
                // queue.offer(task, 1500, TimeUnit.MILLISECONDS);
                // 3) 让调用者放弃任务执行
                // log.debug("放弃{}", task);
                // 4) 让调用者抛出异常
                // throw new RuntimeException("任务执行失败 " + task);
                // 5) 让调用者自己执行任务
                task.run();
            });
            for (int i = 0; i < 4; i++) {
                int j = i;
                threadPool.execute(() -> {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.debug("{}", j);
                });
            }
        }
}

/**
 * 步骤2：自定义任务队列
 * @param <T>
 */
@Slf4j(topic = "BlockingQueue")
class BlockingQueue<T> {
    // 1. 任务队列
    private Deque<T>  queue = new ArrayDeque<>();
    // 2. 锁
    private ReentrantLock lock = new ReentrantLock();
    // 3. 生产者条件变量
    private Condition fullWaitSet = lock.newCondition();
    // 4. 消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();

    // 5. 容量
    private int capcity;

    public BlockingQueue(int capcity) {
        this.capcity = capcity;
    }

    // 带超时时间的获取
    public T poll(long timeout, TimeUnit unit){
        lock.lock();
        try{
            // 将 timeout 统一转换为 纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()){
                try {
                    if (nanos<=0){
                        return null;
                    }
                    // 返回的是剩余的等待时间，更改navos的值，使虚假唤醒的时候可以继续等待
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            fullWaitSet.signal();
            return queue.getFirst();
        }finally {
            lock.unlock();
        }
    }
    
    // 阻塞获取
    public T Take(){
        lock.lock();
        try{
            while (queue.isEmpty()){
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            fullWaitSet.signal();
            return queue.getFirst();
        }finally {
            lock.unlock();
        }
    }
    
    // 阻塞增加
    public void put (T task){
        lock.lock();
        try{
            while (queue.size() == capcity){
                try {
                    log.debug("等待加入任务队列 {} ...", task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
        }finally {
            lock.unlock();
        }
    }

    // 带超时时间的增加
    public boolean offer(T task , long timeout , TimeUnit unit){
        lock.lock();
        try{
            // 将 timeout 统一转换为 纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.size() == capcity){
                try {
                    if (nanos<=0){
                        return false;
                    }
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        }finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try{
            // 不空闲时怎么办？由rejectPolicy决定
            if (queue.size()> capcity){
                rejectPolicy.reject(this, task);
            }else {
                log.debug("加入任务队列 {}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        }finally {
            lock.unlock();
        }
    }
    
    public int size(){
        lock.lock();
        try{
            return queue.size();
        }finally {
            lock.unlock();
        }
    }
}


/**
 * 步骤3：自定义线程池
 */
@Slf4j(topic = "ThreadPool")
class ThreadPool{

    // 任务队列
    private BlockingQueue<Runnable>  taskQueue;

    // 线程集合
    private HashSet<Worker> workers = new HashSet<>();

    // 核心线程数
    int coreSize;
    
    private long timeOut;
    
    private TimeUnit timeUnit;

    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeOut, TimeUnit timeUnit,int capcity
            ,RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeOut = timeOut;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(capcity);
        this.rejectPolicy = rejectPolicy;
    }

    // 执行任务
    public void execute(Runnable task){
        // 当任务数没有超过 coreSize 时，直接交给 worker 对象执行
        // 如果任务数超过 coreSize 时，加入任务队列暂存
        synchronized (workers){
            if (workers.size()<coreSize){
                Worker worker = new Worker(task);
                workers.add(worker);
                worker.start();
            }else {
                // 1) 死等
//                 taskQueue.put(task); 
               
                // 2) 带超时等待
                //taskQueue.tryPut(rejectPolicy, task);
                // 3) 让调用者放弃任务执行
                // 4) 让调用者抛出异常
                // 5) 让调用者自己执行任务

                //或者将以上的这些选项封装起来，由调用者调用时自己设计操作逻辑
                taskQueue.tryPut(rejectPolicy,task);
            }
            
        }
    }
    
    
    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 1) 当 task 不为空，执行任务
            // 2) 当 task 执行完毕，再接着从任务队列获取任务并执行
            while(task!=null ||  (task = taskQueue.poll(timeOut,timeUnit)) != null) {
                try{
                    log.debug("正在执行...{}", task);
                    task.run();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    task = null;
                }
            }
            
            synchronized (workers){
                log.debug("worker 被移除{}", this);
                workers.remove(this);
            }
        }
    }
    
}

/**
 * 步骤1：自定义拒绝策略接口
 * @param <T>
 */
@FunctionalInterface // 拒绝策略   @FunctionalInterface的意思是这是一个函数式编程接口
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}

