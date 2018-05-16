public class CustomQuick3StringEncode {
    private static final int CUTOFF =  20;   // cutoff to insertion sort
    private static int LENGTH;

    // do not instantiate
    private CustomQuick3StringEncode() { }

    public static void sort(char[] a, int[] index, int length) {
        LENGTH = length;
        sort(a, 0, LENGTH-1, index);
    }

    private static char charAt(char[] a, int[] index, int i, int d) {
        return a[(index[i]+d)%LENGTH];
    }

    // 3-way string quicksort a[lo..hi] starting at dth character
    private static void sort(char[] a, int lo, int hi, int[] index) {

        // cutoff to insertion sort for small subarrays
        if (hi < lo + CUTOFF) {
            insertion(a, lo, hi, index);
            return;
        }

        if (isSorted(a, index, lo, hi)) return;

        int lt = lo, gt = hi;
        int i = lo + 1;
        while (i <= gt) {
            if (!compareString(a, lo, i, index)) exch(lt++, i++, index);
            else exch(i, gt--, index);
        }

        // matrix = printarray(a, index, lo, hi, level);
        sort(a, lo, lt-1, index);
        sort(a, gt+1, hi, index);
    }

    // sort from a[lo] to a[hi]
    private static void insertion(char[] a, int lo, int hi, int[] index) {
        if (isSorted(a, index, lo, hi)) return;

        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && !(compareString(a, j-1, j, index)); j--)
                exch(j, j-1, index);

    }

    // exchange a[i] and a[j]
    private static void exch(int i, int j, int[] index) {
        int tempint = index[i];
        index[i] = index[j];
        index[j] = tempint;
    }

    private static boolean compareString(char[] a, int i, int j, int[] index) {
        int cmp = 0;
        for (int k = 0; k<LENGTH; k++) {
            cmp = charAt(a, index, j, k) - charAt(a, index, i, k);
            if (cmp < 0) return false;
            else if (cmp > 0) return true;
        }
        return true;
    }


    // is the array sorted
    private static boolean isSorted(char[] a, int[] index, int lo, int hi) {
        for (int i = lo+1; i < hi+1; i++) {
            if (!compareString(a, i-1, i, index)) return false;
        }
        return true;
    }
}
