package com.hs.util;

public class Utils {

    public static int generateRandomNumber(int i) {
        return (int)(Math.random()*i);
    }
    public static int[] generateRandomNumbers(int i) {
        int[] nums =  new int[i];
        for (int j=0;j<i;++j) {
            nums[j] = (int)(Math.random()*i*2);
        }
        return nums;
    }

    public static void printShort(int[] k, int n) {
        printShort(k, n, 0, k.length);
    }

    public static synchronized void printShort(int[] k, int n, int start, int end) {
        StringBuffer sb = new StringBuffer();
        for (int i=start;i<end && i < start+n; ++i) {
            sb.append(k[i] + (i < n-1 ? ", " : ""));
        }
        if (end-n > n) {
            sb.append(" .... ");
            for (int i=end-n;i<end ; ++i) {
                sb.append(k[i] + (i < end-1 ? ", " : ""));
            }
        }
        System.out.println(sb.toString());
    }
}
