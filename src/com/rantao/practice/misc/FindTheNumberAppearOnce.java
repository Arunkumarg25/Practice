package com.rantao.practice.misc;

import java.util.Hashtable;

public class FindTheNumberAppearOnce {
	
	public void findNum(int[] input){
		int len = input.length;
		if(len <= 0) 
			return;
		if(len == 1)
			System.out.println(input[0]);
		
		Hashtable<Integer, Integer> ht = new Hashtable<Integer, Integer>();
		for(int i = 0; i < len ; i++){
			if(ht.get(input[i]) != null)
				ht.put(input[i], ht.get(input[i])+1);
			else
				ht.put(input[i], 1);
		}
		
		for(int i =0; i < len; i++){
			if(ht.get(input[i]) == 1)
				System.out.println(input[i]);
		}
		
	}
	
	public void findChar(String input){
		if(input == null || input == "")
			return;
		int len = input.length();
		if(len == 1)
			System.out.println(input);
		
		int[] chars = new int[256];
		for(int i =0; i < len; i++){
			chars[input.charAt(i)]++;
		}
		for(int i = 0; i < len; i++){
			if(chars[input.charAt(i)] == 1)
				System.out.println(input.charAt(i));
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FindTheNumberAppearOnce ftao = new FindTheNumberAppearOnce();
		int[] input = {1,1,1,1,1,1,2,2,2,2,2,3,0,30,30,2,4,5,6,7};
		String s = "aabbcccdefek";
		ftao.findNum(input);
		ftao.findChar(s);

	}

}
