package com.ran.fun;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Given a collection of integers that might contain duplicates, S, return all possible subsets.
 * 
 * Note:
 * 
 * Elements in a subset must be in non-descending order. The solution set must not contain duplicate subsets. For
 * example, If S = [1,2,2], a solution is:
 * 
 * [ [2], [1], [1,2,2], [2,2], [1,2], [] ]
 * 
 * @author taor
 * @since Aug 7, 2013
 */

public class SubSetsII {
    
    public static ArrayList<Integer> copy(ArrayList<Integer> ori) {

        ArrayList<Integer> result = new ArrayList<Integer>();
        for (Integer i : ori) {
            result.add(i);
        }
        return result;
    }

    public static ArrayList<Integer> sort(ArrayList<Integer> ori) {

        Collections.sort(ori);
        return ori;
    }

    public static ArrayList<ArrayList<Integer>> subsets(int[] S) {

        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();

        if (S.length == 0) {

            ArrayList<Integer> result = new ArrayList<Integer>();
            results.add(result);
            return results;
        }

        Integer head = S[0];
        int[] leftIntegers = new int[S.length - 1];
        for (int i = 1; i < S.length; i++) {

            leftIntegers[i - 1] = S[i];
        }

        ArrayList<ArrayList<Integer>> lastResults = subsets(leftIntegers);
        for (ArrayList<Integer> lastResult : lastResults) {

            ArrayList<Integer> newResult = copy(lastResult);
            results.addAll(lastResults);
            newResult.add(head);
            newResult = sort(newResult);
            results.add(newResult);
        }

        return results;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

    }
}
