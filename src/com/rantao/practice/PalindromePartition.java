package com.ran.fun;

import java.util.ArrayList;

/**
 * Given a string s, partition s such that every substring of the partition is a palindrome.
 * 
 * Return all possible palindrome partitioning of s.
 * 
 * For example, given s = "aab", Return
 * 
 * [ ["aa","b"], ["a","a","b"] ]
 * 
 * @author taor
 * @since Jul 24, 2013
 */

public class PalindromePartition {

    /**
     * I copied this part of code from somewhere else. This is using DP to solve the issue. Succinct and beautiful.
     * 
     * @param str
     * @param begin
     * @param pt
     * @param results
     */
    public static void findPartition(String str, int begin, ArrayList<String> pt, ArrayList<ArrayList<String>> results) {

        // copy the partition from pt to copy and add it to result set
        if (begin >= str.length()) {
            ArrayList<String> copy = new ArrayList<String>();
            for (int j = 0; j < pt.size(); j++) {
                copy.add(pt.get(j));
            }
            results.add(copy);
        }

        // back tracking
        for (int i = begin; i < str.length(); i++) {
            if (isPalindrome(str, begin, i)) {
                pt.add(str.substring(begin, i + 1));
                findPartition(str, i + 1, pt, results);
                //remove the last empty one - when begin = i
                pt.remove(pt.size() - 1);
            }
        }
    }

    public static boolean isPalindrome(String s, int start, int end) {

        while (start < end) {
            if (s.charAt(start) != s.charAt(end)) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }

    public static void main(String[] args) {

        String str = "aabbccdj";
        ArrayList<String> pt = new ArrayList<String>();
        ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
        findPartition(str, 0, pt, results);

        for (ArrayList<String> re : results) {

            for (int i = 0; i < re.size(); i++) {
                System.out.println(re.get(i));
            }
            System.out.println("--------------");
        }
    }
}
