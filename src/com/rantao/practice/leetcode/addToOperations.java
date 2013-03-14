package com.rantao.practice.leetcode;

public class addToOperations {
	public int negative(int input){
		int sign = (input > 0) ? -1 : 1;
		int result = 0;
		while(input != 0){
			result = sign++;
			input = input + sign;
		}
		return result;
	}
	
	public int substract(int a, int b){
		b = this.negative(b);
		return a+b;
	}
	
	public int abs(int a){
		if(a > 0)
			return a;
		else 
			return negative(a);
	}
	
	public boolean positiveOrNegative(int a, int b){
		if((a >0 && b >0) ||(a<0 && b <0))
			return true;
		else
			return false;
	}
	
	public int multiple(int a, int b){
		int absa = abs(a);
		int absb = abs(b);
		int result = 0;
		
		for(int i = 0; i < absa; i ++){
			result = result + absb;
		}
		
		if(positiveOrNegative(absa, absb)){
			return result;
		}else
			return negative(result);				
	}
	
	public int divide(int a, int b){
		int result = 0;
		int count = 0;
		a = abs(a);
		b = abs(b);
		
		while(a > 0){
			a = a-b;
			count++;
		}
		
		if(!positiveOrNegative(a, b))
			result = negative(count);
		else
			result = count;
		
		return result;
	}
}
