package com.rantao.practice.misc;

public class ReverseString {
	
	public static String reverse(String s){
		String results ="";
		char[] array = s.toCharArray();
		for(int i = array.length -1; i >= 0; i--){
			results += array[i];
		}
		System.out.println(results);
		return results;
	}	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = "abcdefg";
		reverse(s);
	}
}
