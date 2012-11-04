package com.rantao.practice.misc;

public class StringCompression {
	public static String strCompression(String s){
		String results = "";
		char[] array = s.toCharArray();
		int len = array.length;
		int pointer = 0;
		int count = 0;
		for(int i = 0; i< len; i++){
			if(array[pointer]!=array[i]){
				results = results + array[pointer] + count;
				pointer = i;
				count = 1;
			}else
				count++;
		}
		results = results + array[pointer]+count;
		if(results.length() > s.length())
			results = s;
		System.out.println(results);
		return results;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s ="aaabbbcccddeef";
		strCompression(s);
		

	}

}
