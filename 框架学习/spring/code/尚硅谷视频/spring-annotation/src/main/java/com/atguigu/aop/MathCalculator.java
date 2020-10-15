package com.atguigu.aop;

public class MathCalculator {
	
	public int div(int i,int j){
		System.out.println("MathCalculator...div...");
		return i/j;	
	}


	public void div2(){
		System.out.println("这个不用使用aop");
	}

}
