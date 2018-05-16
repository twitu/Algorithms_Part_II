import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static int first;

    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circarray = new CircularSuffixArray(s);
        for (int i=0; i<s.length(); i++) {
            if (circarray.index(i)==0) {
                first = i;
                break;
            }
        }
        BinaryStdOut.write(first);
        for (int i=0; i<s.length(); i++) {
            BinaryStdOut.write(s.charAt((circarray.index(i)-1+s.length())%s.length()));
        }
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();


        StringBuilder sb = new StringBuilder(s.length());

        char[] letters = s.toCharArray();
        int[] index = new int[s.length()];
        for (int i=0; i<s.length(); i++) {
            index[i] = i;
        }

        CustomQuick3StringDecode.sort(letters, index);

        for (int i=s.length()-1; i>-1; i--) {
            sb.append(letters[first]);
            first = index[first];
        }
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.close();
    }

    public static void main (String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal argument given.");
    }
}
