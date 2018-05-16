public class CircularSuffixArray {
    private String original;
    private int[] index;

    private void constructSortSuffixes() {
        CustomQuickStringEncode.sort(original.toCharArray(), index, original.length());
    }

    public CircularSuffixArray (String s) {
        if (s==null) throw new IllegalArgumentException("Illegal argument given.");
        original = s;
        index = new int[s.length()];

        for (int i=0; i<original.length(); i++) {
            index[i] = i;
        }
        constructSortSuffixes();
    }

    public int length() {
        return original.length();
    }

    public int index(int i) {
        if (i<0 || i>= length()) throw new IllegalArgumentException("Illegal argument given.");
        return index[i];
    }

    public static void main(String[] args) {
//        String newstring = "ABRACADABRA!";
//        CircularSuffixArray cirarray = new CircularSuffixArray(newstring);
//        for (int i=0; i<newstring.length(); i++) {
//            StdOut.println(cirarray.index(i) + " " + newstring.charAt((cirarray.index(i)+newstring.length()-1)%newstring.length()));
//        }
    }
}
