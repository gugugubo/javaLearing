package com.gcb.concurrency1;

public class Client {
    public static void main(String[] args) {
        MyObject myObject = new MyObject();
        DecreaseThread decreaseThread = new DecreaseThread(myObject);
        IncreaseThread increaseThread = new IncreaseThread(myObject);
        increaseThread.start();
        decreaseThread.start();
    }
}
