package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;


@Slf4j
public class Test16 {
    // Random 是线程安全的，多个线程调用此对象的同一个方法是不会有问题哒
    static Random random = new Random();

    public static int randomAmount(){
        return random.nextInt(5)+1;
    }



    public static void main(String[] args) {
        // 此类不是线程安全的，但是此变量不在线程间共享！
        ArrayList<Thread> arrayList = new ArrayList<>();

        // 此处会出现问题，因为调用此对象的buy方法时就会多线程共享一个totalTicket 变量
        BuyTicket buyTicket = new BuyTicket(10000);


        // 此类是线程安全的类
        List<Integer> list = new Vector();

        for(int i=0; i<2000;i++){
            Thread thread = new Thread(() -> {
                int buy = buyTicket.buy(randomAmount());
                list.add(buy);
            }, "我是线程" + i);
            arrayList.add(thread);
            thread.start();
        }

        arrayList.forEach((t) -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 买出去的票求和
        log.debug("selled count:{}",list.stream().mapToInt(c -> c).sum());
        // 剩余票数
        log.debug("remainder count:{}", buyTicket.getTotalTicket());
    }


}

class BuyTicket{
    private  int totalTicket;


    public BuyTicket(int totalTicket){
        this.totalTicket = totalTicket;
    }

    public int getTotalTicket(){
        return this.totalTicket;
    }

    // 解决方法：可以在此方法上加上一个synchronized ！！
    public int buy(int ticketNum){
        if (totalTicket> ticketNum){
            totalTicket = totalTicket - ticketNum;
            return ticketNum;
        }
        return 0;
    }


}
