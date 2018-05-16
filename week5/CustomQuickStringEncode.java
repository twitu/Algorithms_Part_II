public class CustomQuickStringEncode {
    private static final int CUTOFF =  5;   // cutoff to insertion sort
    private static int LENGTH;

    // do not instantiate
    private CustomQuickStringEncode() { }

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

        int i = lo;
        int j = hi + 1;
        int v = lo;

        while (true) {

            // find item on lo to swap
            while (lessString(a, ++i, v, index)) {
                if (i == hi) break;
            }

            // find item on hi to swap
            while (lessString(a, v, --j, index)) {
                if (j == lo) break;      // redundant since a[lo] acts as sentinel
            }

            // check if pointers cross
            if (i >= j) break;

            exch(i, j, index);
        }

        // put partitioning item v at a[j]
        exch(lo, j, index);

        sort(a, lo, j-1, index);
        sort(a, j+1, hi, index);
    }

    // sort from a[lo] to a[hi]
    private static void insertion(char[] a, int lo, int hi, int[] index) {
        if (isSorted(a, index, lo, hi)) return;

        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && !(lessString(a, j-1, j, index)); j--)
                exch(j, j-1, index);

    }

    // exchange a[i] and a[j]
    private static void exch(int i, int j, int[] index) {
        int tempint = index[i];
        index[i] = index[j];
        index[j] = tempint;
    }

    private static boolean lessString(char[] a, int i, int j, int[] index) {
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
            if (!lessString(a, i-1, i, index)) return false;
        }
        return true;
    }
}
