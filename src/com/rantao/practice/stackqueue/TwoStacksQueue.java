package com.rantao.practice.stackqueue;

import java.util.Stack;

public class TwoStacksQueue {
	Stack<Integer> stack1 = new Stack<Integer>();;
	Stack<Integer> stack2 = new Stack<Integer>();;
	
	public TwoStacksQueue(){
		super.getClass();
	}
	
	public void enQueue(int m){
		stack2.push(stack1.pop());
		stack1.push(m);
	}
	
	public Integer deQueue(int m){
		return stack1.pop();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
