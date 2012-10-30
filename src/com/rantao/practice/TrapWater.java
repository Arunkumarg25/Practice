package com.rantao.practice;
public class TrapWater
{
    static int water(int[] arr)
    {
        int i = 0;
        int j = arr.length - 1;
        int count = 0;
 
        while (i < j)
        {
            while (i < j && arr[i] < arr[i + 1])
                i++;
 
            while (j > i && arr[j] < arr[j - 1])
                j--;
 
            if (i == j)
                break;
 
            int start;
            int low;
            boolean direct = true;
            if (arr[i] <= arr[j])
            {
                start = i + 1;
                low = arr[i];
                direct = true;
            }
            else
            {
                start = j - 1;
                low = arr[j];
                direct = false;
            }
 
            while (arr[start] < low)
            {
                count += low - arr[start];
 
                if (direct)
                    start++;
                else
                    start--;
            }
 
            if (direct)
                i = start;
            else
                j = start;
        }
 
        return count;
 
    }
 
    public static void main(String[] args)
    {
        int[][] q = new int[][]
        {
        { 3, 1, 5 },
        { 3, 1, 0, 5 },
        { 1, 2, 3 },
        { 3, 2, 1 },
        { 1, 2, 3, 2, 1 },
        { 5, 4, 3, 6, 2, 3 },
        { 0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1 } };
        int[] a = new int[]
        { 2, 5, 0, 0, 0, 4, 6 };
 
        for (int i = 0; i < q.length; i++)
        {
            int r = water(q[i]);
            if (a[i] == r)
                System.out.print("[Pass]");
            else
                System.out.print("[Fail]");
 
            System.out.println("expected:" + a[i] + " actual:" + r);
        }
    }
}