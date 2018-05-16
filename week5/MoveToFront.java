import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    public static void encode() {
        char[] chararray = new char[R];
        char count;
        for (count=0; count<R; count++) {
            chararray[count] = count;
        }

        char read;
        while (!BinaryStdIn.isEmpty()){
            read = BinaryStdIn.readChar();
            for (count=0; count<R; count++) {
                if (chararray[count]==read) {
                    BinaryStdOut.write(count);
                    while (count > 0) {
                        chararray[count] = chararray[--count];
                    }
                    chararray[count] = read;
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] chararray = new char[R];
        for (int i=0; i<R; i++) {
            chararray[i] = (char) i;
        }

        char read;
        char pos;
        int count;
        while (!BinaryStdIn.isEmpty()) {
            read = BinaryStdIn.readChar();
            pos = chararray[read];
            count = read;
            while (count > 0) {
                chararray[count] = chararray[--count];
            }
            chararray[count] = pos;
            BinaryStdOut.write(pos);
        }

        BinaryStdOut.close();
        BinaryStdIn.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal argument given.");
    }
}
