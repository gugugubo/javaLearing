package com.concurrent.test2;

import sun.misc.Unsafe;

public class Test16 implements Account{

    public static void main(String[] args) {
        
        Account.demo(new Test16(10000));
    }
    
    
    private volatile int value;

    private static final Unsafe unsafe;
    
    private static long valueOffset;

    public Test16(int value) {
        this.value = value;
    }

    static {
        unsafe = Test14.getUnsafe();
        try {
            valueOffset = unsafe.objectFieldOffset(Test16.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    
    public int getValue(){
        return this.value;
    }
    
    public void decrement(int amount){
        while (true){
            int pre = getValue();
            int next = pre - amount;
            if (unsafe.compareAndSwapInt(this,valueOffset,pre,next)){
                break;
            }
        }
    }

    @Override
    public Integer getBalance() {
        return getValue();
    }

    @Override
    public void withdraw(Integer amount) {
        decrement(amount);
    }
}
