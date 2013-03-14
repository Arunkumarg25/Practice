package com.rantao.practice.leetcode;

public class ClimbStairs {
	
	public int climbStairs(int n) {
		
		if(n>2){
			return climbStairs(n - 2) + climbStairs(n - 1);
		}else if (n == 2) {
			return 2;
		} else if (n == 1) {
			return  1;
		} else 
			return 0;
	}

	public static void main(String[] args) {
		ClimbStairs cs = new ClimbStairs();
		int i = cs.climbStairs(5);
		System.out.println(i);
	}
}
