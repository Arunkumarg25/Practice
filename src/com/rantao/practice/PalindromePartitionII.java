package com.ran.fun;

/**
 * Given a string s, partition s such that every substring of the partition is a palindrome.
 * 
 * Return the minimum cuts needed for a palindrome partitioning of s.
 * 
 * For example, given s = "aab", Return 1 since the palindrome partitioning ["aa","b"] could be produced using 1 cut.
 * 
 * @author taor
 * @since Aug 7, 2013
 */

public class PalindromePartitionII {

    public static Boolean isPalindrome(String str) {

        if (str == null || str == "") {
            return true;
        }

        int i = 0;
        int j = str.length() - 1;

        while (i < j) {
            if (str.charAt(i) == str.charAt(j)) {
                i++;
                j--;
            }
            else {
                return false;
            }
        }
        return true;
    }

    public static int findMinCutPartition(String str, int min) {

        if (str == null || str == "" || str.length() == 1) {
            return 0;
        }

        String head = str.valueOf(0);
        
              
        
        return min;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

    }
}
