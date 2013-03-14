package com.rantao.practice.leetcode;

import java.util.HashSet;

public class LongestSubstringWithoutRepeatingCharacters {
	public int lengthOfLongestSubstring(String s) {
		HashSet<Character> set = new HashSet<Character>();
		int max = 0;
		int candidateStartIndex = 0;
		for (int index = 0; index < s.length(); index++) {
			char c = s.charAt(index);
			// if the set contains the character, adjust the max
			if (set.contains(c)) {
				max = Math.max(max, index - candidateStartIndex);
				// 
				while (s.charAt(index) != s.charAt(candidateStartIndex)) {
					set.remove(s.charAt(candidateStartIndex));
					candidateStartIndex++;
				}
				candidateStartIndex++;
			} else {
				set.add(c);
			}
		}
		max = Math.max(max, s.length() - candidateStartIndex);
		return max;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "aabbccdd";
		String st = "aaaaa";
		String str = "abcabcbb";
		

	}

}
