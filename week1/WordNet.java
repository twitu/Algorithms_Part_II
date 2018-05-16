import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;

import java.util.ArrayList;
import java.util.Iterator;

public class WordNet {
    private int count;
    private final LinearProbingHashST<String, ArrayList<Integer>> synset;
    private final LinearProbingHashST<Integer, String> nouns;
    private final Digraph hypernym;
    private final SAP sap;
    private DirectedCycle cycle;

    public WordNet(String synsets, String hypernyms) {
        this.count = 0;
        In insyn = new In(synsets);
        synset = new LinearProbingHashST<>();
        nouns = new LinearProbingHashST<>();
        while (insyn.hasNextLine()) {
            String[] syn = insyn.readLine().split(",");
            String[] noun = syn[1].split(" ");
            int synid = Integer.parseInt(syn[0]);
            nouns.put(synid, syn[1]);
            for (int i=0; i<noun.length; i++) {
                if (synset.contains(noun[i])) {
                    synset.get(noun[i]).add(synid);
                } else {
                    synset.put(noun[i], new ArrayList<Integer>());
                    synset.get(noun[i]).add(synid);
                }
            }
            count++;
        }


        In inhyp = new In(hypernyms);
        hypernym = new Digraph(count);
        while (inhyp.hasNextLine()) {
            String[] relation = inhyp.readLine().split(",");
            Integer hypo = Integer.parseInt(relation[0]);
            for (int i = 1; i < relation.length; i++) {
                hypernym.addEdge(hypo, Integer.parseInt(relation[i]));
            }

        }

        this.sap = new SAP(this.hypernym);

        cycle = new DirectedCycle(hypernym);
        if (cycle.hasCycle()) throw new IllegalArgumentException("The given graph forms a directed cyclic graph.");

    }

    public Iterable<String> nouns() {
        Iterator<String> key = synset.keys().iterator();
        ArrayList<String> noun = new ArrayList<>();
        while (key.hasNext()) {
            noun.add(key.next());
        }
        return noun;
    }

    public boolean isNoun(String word) {
        return synset.contains(word);
    }

    public int distance(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            return this.sap.length(synset.get(nounA), synset.get(nounB));
        } else {
            throw new IllegalArgumentException("The given nouns are not available.");
        }
    }

    public String sap(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            int ancestorVal = this.sap.ancestor(synset.get(nounA), synset.get(nounB));
            return nouns.get(ancestorVal);
        } else {
            throw new IllegalArgumentException("The given nouns are not available.");
        }
    }

    public static void main(String[] args) {

    }
}
