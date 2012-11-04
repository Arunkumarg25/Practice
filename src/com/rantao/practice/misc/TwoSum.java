package com.rantao.practice.misc;

import java.util.Arrays;

public class TwoSum {

	public static void TwoSum(int[] input, int Sum){
		if(input == null)
			return;
		int len = input.length;
		if(len == 1)
			return;
		Arrays.sort(input);
		int head = 0;
		int tail = len-1;
		while(head < tail){
			if(input[head] == Sum - input[tail]){
				System.out.println("One group: " + input[head] +", "+ input[tail]);
				head++;
			}
			else if(input[head] < Sum - input[tail])
				head++;
			else if(input[head] > Sum - input[tail])
				tail--;
		}
	}
	
	public static void main(String[] args) {
		int[] input = {3, -1, 6, 7, -9, 4, 5, 7, 2, 0, 3};
		int Sum = 6;
		TwoSum(input, Sum);

	}

}
