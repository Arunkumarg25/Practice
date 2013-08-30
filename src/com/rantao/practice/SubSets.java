package com.ran.fun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*Given a set of distinct integers, S, return all possible subsets.

 Note:

 Elements in a subset must be in non-descending order.
 The solution set must not contain duplicate subsets.
 For example,
 If S = [1,2,3], a solution is:

 [
 [3],
 [1],
 [2],
 [1,2,3],
 [1,3],
 [2,3],
 [1,2],
 []
 ]*/

public class SubSets {

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

        int[] S = {1, 2, 2};
        ArrayList<ArrayList<Integer>> results = subsets(S);

        // remove duplicated sets
        Set<ArrayList<Integer>> sets = new HashSet<ArrayList<Integer>>();
        sets.addAll(results);
        results.clear();
        results.addAll(sets);

        System.out.println(results.toString());
        System.out.print("done");
    }

}
