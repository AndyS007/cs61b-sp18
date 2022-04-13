package hw3.hash;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO:
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        //Map<Integer, Integer> hashMap = new HashMap();

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            list.add(0);
        }
        int N = oomages.size();
        for (Oomage oomage : oomages) {
            int bucketNum = (oomage.hashCode() & 0x7FFFFFFF) % M;
            list.set(bucketNum, list.get(bucketNum) + 1);
        }
        for (int count : list) {
            if (N / 50 > count || count > N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
