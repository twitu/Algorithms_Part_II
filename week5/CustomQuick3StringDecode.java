public class CustomQuick3StringDecode {
    private static final int CUTOFF =  12;   // cutoff to insertion sort

    // do not instantiate
    private CustomQuick3StringDecode() { }

    public static void sort(char[] array, int[] index) {
        sort(array, 0, array.length-1, index);
    }

    // 3-way string quicksort a[lo..hi] starting at dth character
    private static void sort(char[] array, int lo, int hi, int[] index) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(array, lo, hi, index);
            return;
        }

        int lt = lo, gt = hi;
        char v = array[lo];
        int i = lo + 1;
        char t;
        while (i <= gt) {
            t = array[i];
            if      (t < v) exch(lt++, i++, index, array);
            else if (t > v) exch(i, gt--, index, array);
            else            i++;
        }

        sort(array, lo, lt-1, index);
        sort(array, gt+1, hi, index);
    }

    // sort from a[lo] to a[hi]
    private static void insertion(char[] array, int lo, int hi, int[] index) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && array[j] < array[j-1]; j--)
                exch(j, j-1, index, array);
    }

    // exchange a[i] and a[j]
    private static void exch(int i, int j, int[] index, char[] array) {
        char temp = array[i];
        array[i] = array[j];
        array[j] = temp;

        int tempint = index[i];
        index[i] = index[j];
        index[j] = tempint;
    }

}
