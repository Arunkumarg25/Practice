package com.rantao.practice;


public class FindOnlyChar {
	public char firstOnlyCharacter(String A) {  
        int[] array = new int[256];  
          
        //store the characters in A to array  
        for (int i = 0; i < A.length(); i++) {  
            array[A.charAt(i)] += 1;  
        }  
          
        //get the first character with only one appearance in A  
        for (int i = 0; i < A.length(); i++) {  
            if (array[A.charAt(i)] == 1) return A.charAt(i);  
        }  
        
        return 0;  
	}
	
	public static void main(String[] args){
		String A = "aaabccc";
		FindOnlyChar foc = new FindOnlyChar();
		char c = foc.firstOnlyCharacter(A);
		System.out.println(c);
	}
}
