import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph graph;
    private static BreadthFirstDirectedPaths bfssource;
    private static BreadthFirstDirectedPaths bfsdest;

    public SAP(Digraph G) {
        this.graph = G;
    }

    private boolean isValid(int v) {
        return (v>-1 && v<this.graph.V());
    }

    private boolean isValid(Iterable<Integer> v) {
        for (int i: v) {
            if (i<0 && i>=this.graph.V()) return false;
        }
        return true;
    }

    public int length(int v, int w) {
        if (!isValid(v) || !isValid(w)) throw new IllegalArgumentException("Invalid vertice");

        int ancestorVal = ancestor(v, w);
        if (ancestorVal==-1) return -1;
        return bfssource.distTo(ancestorVal) + bfsdest.distTo(ancestorVal);
    }

    public int ancestor(int v, int w) {
        if (!isValid(v) || !isValid(w)) throw new IllegalArgumentException("Invalid vertice");
        bfssource = new BreadthFirstDirectedPaths(this.graph, v);
        bfsdest = new BreadthFirstDirectedPaths(this.graph, w);

        int ancestorVal = -1;
        int ancestorLength = Integer.MAX_VALUE;
        int candidateLength = 0;

        for (int vertice=0; vertice<this.graph.V(); vertice++) {
            if (bfssource.hasPathTo(vertice) && bfsdest.hasPathTo(vertice)) {
                candidateLength = bfssource.distTo(vertice) + bfsdest.distTo(vertice);
                if (candidateLength < ancestorLength) {
                    ancestorVal = vertice;
                    ancestorLength = candidateLength;
                }
            }
        }
        return ancestorVal;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v==null || w==null) throw new IllegalArgumentException("Null arguments are not allowed.");

        int ancestorVal = ancestor(v, w);
        if (ancestorVal==-1) return -1;
        return bfssource.distTo(ancestorVal) + bfsdest.distTo(ancestorVal);
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v==null || w==null) throw new IllegalArgumentException("Null arguments are not allowed.");
        if (!isValid(v) || !isValid(w)) throw new IllegalArgumentException("Invalid vertice");
        bfssource = new BreadthFirstDirectedPaths(this.graph, v);
        bfsdest = new BreadthFirstDirectedPaths(this.graph, w);

        int ancestorVal = -1;
        int ancestorLength = Integer.MAX_VALUE;
        int candidateLength = 0;

        for (int vertice=0; vertice<this.graph.V(); vertice++) {
            if (bfssource.hasPathTo(vertice) && bfsdest.hasPathTo(vertice)) {
                candidateLength = bfssource.distTo(vertice) + bfsdest.distTo(vertice);
                if (candidateLength < ancestorLength) {
                    ancestorVal = vertice;
                    ancestorLength = candidateLength;
                }
            }
        }
        return ancestorVal;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
