package com.rantao.practice.misc;

import java.util.ArrayList;

public class IsContain {
	public static ArrayList<Integer> findSub(String str1, String str2) {
		char[] str1Arr = str1.toCharArray();
		char[] str2Arr = str2.toCharArray();

		int maxIndex1 = str1.length()-1;
		int maxIndex2 = str2.length()-1;

		ArrayList<Integer> B = new ArrayList<Integer>();
		// Boolean b = false;
		// abc a
		for (int i = 0; i < maxIndex1; i++) {
			int temp = i;
			if (str1Arr[i] == str2Arr[0]) {
				for (int j = 0; j <= maxIndex2; j++) {
					if (str1Arr[i] == str2Arr[j]) {
						if(j == maxIndex2)
							B.add(i);
						else {
							i++;
							continue;
						}						
					} else {
						break;
					}
				}
				i = temp;
			}
		}

		java.util.Iterator<Integer> ib = B.iterator();
		while (ib.hasNext()) {
			System.out.println(ib.next());
		}
		return B;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = "testatesttest";
		String b = "test";
		findSub(a, b);

	}

}
