/*
 * Solution for Project Euler 113
 * http://projecteuler.net/problem=113
 * 
 * Non-bouncy numbers
 * Working from left-to-right if no digit is exceeded by the digit to its left
 * it is called an increasing number; for example, 134468.  Similarly if no
 * digit is exceeded by the digit to its right it is called a decreasing number;
 * for example, 66420.  We shall call a positive integer that is neither
 * increasing nor decreasing a "bouncy" number; for example, 155349.
 * 
 * As n increases, the proportion of bouncy numbers below n increases such that
 * there are only 12951 numbers below one-million that are not bouncy and only
 * 277032 non-bouncy numbers below 10^10.
 * 
 * How many numbers below a googol (10^100) are not bouncy?
 * 
 * Author: Jason LaFrance
 * 
 * Using recursion trees and memory arrays to avoid recounting numbers minimizes
 * execution time exponentially.  Just count increasing, decreasing, and flat
 * numbers.
 */

import java.util.ArrayList;

public class E113 {
    public static void main(String[] args) {
        ArrayList<Integer> d = new ArrayList<>();
        
        // declare/initialize seperate arrays for increasing and decreasing
        // numbers.  First index is digit and second is number magnitude
        long [][] inc = new long [10][101];
        long [][] dec = new long [10][101];

        long count = 0;
        long start = System.currentTimeMillis();
        for(int width=1; width<=100; width++){
            long s = 0;
            s += incRecurse(1, width, 1, inc);    // start the increasing number tree
            s += decRecurse(1, width, 9, dec);    // start the decreasing number tree
            s -= 9;                               // remove the overlap
            count += s;                         // and add total to count
        }
        long stop = System.currentTimeMillis();
        System.out.println(count);
        System.out.println("Done in " + (stop - start) / 1000.0 + " seconds.");
    }
    
    /* both recurse functions have the same argument structure
     * level: current number magnitude
     * width: maximum number magnitude
     * d: current digit
     * l: memory array
     */
    public static long incRecurse(int level, int width, int d, long [][] l){
        long ret = 0;
        // if at max width, just return remaining digit count
        if(level == width) return 10 - d;
        // only do this block if the memory array has no entry for
        // this digit/magnitude combo, otherwise, it's already been calculated
        if(l[d][width-level] == 0){      
            long sum = 0;
            // recurse in and count all lower digit/magnitude combos
            for(int i = d; i < 10; i++){
                sum += incRecurse(level + 1, width, i, l);
            }
            // set memory array position to sum
            l[d][width-level] = sum;
        }
        // add digit/magnitude sum to return sum
        ret += l[d][width-level];
        return ret;
    }

    
    public static long decRecurse(int level, int width, int d, long [][] l){
        long ret=0;
        // if at max width, just return remaining digit count
        if(level == width){
            if(level > 1) return d + 1; else return d;
        }
        long sum = 0;
        if(level == 1){
            for(int i = d; i >= 1; i--)
                sum += decRecurse(level + 1, width, i, l);
        } else if(l[d][width-level] == 0){
            // only do this block if the memory array has no entry for
            // this digit/magnitude combo, otherwise, it's already been
            // calculated
            for(int i = d; i >= 0; i--)
                sum += decRecurse(level + 1, width, i, l);
            l[d][width-level] = sum;
        } else sum = l[d][width-level];
        ret += sum;
        return ret;
    }
}
