package com.rantao.practice.stackqueue;

public class CircularQueue {
	private int size = 20;
	private int first;
	private int tail;
	private int[] queue = new int[size-1];
	
	
	public void enQueue(int m){
		queue[tail] =  m;
		if(tail < size -1){
			tail = tail + 1;
		}
		else if(tail == size){
			tail = 0;
		}
	}
	
	public int deQueue(){
		if(first == 0)
			return 0;
		else if(first < size -1){
			int temp = queue[first];
			first = first + 1;
			return temp;
		}
		else {
			int temp = queue[first];
			first = 0;
			return temp;
		}
		
	}

	public static void main(String[] args) {
		

	}

}
