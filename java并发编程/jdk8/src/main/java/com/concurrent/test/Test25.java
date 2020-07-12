package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Slf4j(topic = "main")
public class Test25 {
    public static void main(String[] args) {
        // 容量比生产者生产的少一点可以看到队列满的效果
        MessageQueue messageQueue = new MessageQueue(2);

        for (int i =0;i<100;i++){
            // 这里是因为lambda表达式只能放一个final类型的变量，int i在不断变化
            //或者如果没有定义成final，那么变量在初始化以后，不允许再有任何赋值的情况出现
            int id = i;
            new Thread(()->{
                log.debug("download...");
                List<String> response=utils.download();
                log.debug("try put message({})", id);
                messageQueue.put(new Message(id, response));
            },"生产者" + id).start();
        }

        // 1 个消费者线程, 处理结果
        new Thread(()->{
           while (true){
               Message message = messageQueue.take();
               List<String> response = (List<String>) message.getMessage();
               log.debug("take message({}): [{}] lines", message.getId(), response.size());
           }
        },"消费者").start();
    }
    
}


@Slf4j(topic = "MessageQueue")
class MessageQueue{
    private LinkedList<Message> queue;
    private int capacity;
    public MessageQueue( int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }
    
    public Message take(){
        synchronized (queue){
            while (queue.size()==0){
                log.debug("没货了, wait");
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = queue.removeLast();
            queue.notifyAll();
            return message;
        }
    }
    
    public void put(Message message){
        synchronized (queue){
            while (queue.size()==capacity){
                try {
                    log.debug("库存已达上限, wait");
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(message);
            queue.notifyAll();
        }
    }
}

class Message{
    private int id;
    private Object message;
    public Message(int id, Object message) {
        this.id = id;
        this.message = message;
    }
    public int getId() {
        return id;
    }
    public Object getMessage() {
        return message;
    }
}
