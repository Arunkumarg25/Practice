package com.rantao.practice.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Given an array of strings, return all groups of strings that are anagrams.
//Note: All inputs will be in lower-case.

public class FindAnagrams {

	public ArrayList<String> anagrams(String[] strs) {
		Map<String, Map<Character, Integer>> map1 = new HashMap<String, Map<Character, Integer>>();
		Map<Map<Character, Integer>, Integer> map2 = new HashMap<Map<Character, Integer>, Integer>();
		ArrayList<String> list = new ArrayList<String>();
		for (String str : strs) {
			// temp is to store the map of each string and their characters
			Map<Character, Integer> temp = new HashMap<Character, Integer>();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				temp.put(c, temp.get(c) == null ? 1 : temp.get(c) + 1);
			}
			map1.put(str, temp);
			map2.put(temp, map2.get(temp) == null ? 1 : map2.get(temp) + 1);
		}
		for (String str : strs) {
			if (map2.get(map1.get(str)) > 1)
				list.add(str);
		}
		return list;
	}

	public static void main(String[] args) {

	}

}
