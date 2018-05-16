import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {
        int[] outcastdist = new int[nouns.length];
        int outcast = 0;
        int dist = 0;
        int max = 0;
        int max_index = 0;

        for (int i=0; i< nouns.length; i++) {
            for (int j=i+1; j<nouns.length; j++) {
                dist = wordNet.distance(nouns[i], nouns[j]);
                outcastdist[i] += dist;
                outcastdist[j] += dist;
            }
        }
        for (int i=0; i<outcastdist.length; i++) {
            if (outcastdist[i] > max) {
                max = outcastdist[i];
                max_index = i;
            }
        }
        return nouns[max_index];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
