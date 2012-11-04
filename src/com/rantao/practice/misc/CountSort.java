package com.rantao.practice.misc;

public class CountSort {
	public static int[] sort(int[] input){
		int len = input.length;
		int R = 256;
		int temp[] = new int[R];
		int result[] = new int[len];
		for(int i =0; i< len; i++){
			temp[input[i]] ++;
		}
		for(int j=1; j< R; j++){
			temp[j] = temp[j] + temp[j-1];
		}
		for(int k=0; k<len; k++){
			result[temp[input[k]]-1] = input[k];
		}
		return result;		
	}
	
	public static void main(String[] args){
		int[] a= {1,4,8,3,16,2,0,67,5};
		int[] b = sort(a);
		for(int i : b){
			System.out.println(i);
		}
	}
}
