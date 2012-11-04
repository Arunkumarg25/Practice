package com.rantao.practice.misc;
import java.util.HashSet;


public class FindMagicIndex {
	// All instinct
	public static void printPairs(int []a,int x){
		HashSet<Integer> h= new HashSet<Integer>();
		for(int i=0; i < a.length-1; i++){
			h.add(a[i]);
		}
		for(int i=0; i < a.length-1; i++){
			if(h.contains(x-a[i])){
				System.out.println(a[i]+" "+(x-a[i]));
				h.remove(x-a[i]);
			}
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a={1,2,3,4,5,6,7,8,9};
		printPairs(a,9);
	}
	
	
}
