package com.rantao.practice.careercup;

import java.util.Arrays;

public class IsUnique {
	
	public static Boolean isUni(String input){
		Boolean b = true;
		
		char[] inputArr = input.toCharArray();
		Arrays.sort(inputArr);
		for(int i = 0; i < inputArr.length; i++){
			int j = i+1;
			if(j < inputArr.length){
				if(inputArr[i] == inputArr[j]){
					b=false;
					break;
				}else{
					continue;
				}
					
			}
		}
		
		return b;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a ="aaabbbcccddd";
		String b = "abcd";
		System.out.println(isUni(a));
		System.out.println(isUni(b));

	}

}
