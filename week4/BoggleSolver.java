import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private CustomTrie<Integer> wordtrie;
    private int xdimensions;
    private int ydimensions;
    private static boolean[][] visited;
    private static int[] xdis = {-1, 0, 1, -1, 1, -1, 0, 1};
    private static int[] ydis = {-1, -1, -1, 0, 0, 1, 1, 1};

    public BoggleSolver(String[] dictionary) {
        wordtrie = new CustomTrie<>();

        for (String word: dictionary) {
            wordtrie.put(word, scoreOf(word));
        }
    }

    private class VisitedChar {
        private int x;
        private int y;

        private VisitedChar(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> validWord = new SET<>();
        visited = new boolean[board.rows()][board.cols()];
        xdimensions = board.cols();
        ydimensions = board.rows();
        int level = 0;

        VisitedChar vchar = new VisitedChar(0,0);
        for (int i=0; i<xdimensions; i++) {
            for (int j=0; j<ydimensions; j++) {
                StringBuilder buildword = new StringBuilder();
                vchar.x = i;
                vchar.y = j;
                visited[j][i] = true;
                getValidWords(board, validWord, vchar, buildword, level+1);
                visited[j][i] = false;
            }
        }

        return validWord;
    }

    private boolean validIndex(int x, int y) {
        if (x<0 || x>=xdimensions || y<0 || y>=ydimensions) return false;
        else return true;
    }

    private void getValidWords(BoggleBoard board, SET<String> validWord, VisitedChar vchar, StringBuilder substring, int level) {
        if (board.getLetter(vchar.y, vchar.x)=='Q') {
            substring.append("QU");
            level++;
        } else {
            substring.append(board.getLetter(vchar.y, vchar.x));
        }
        String word = substring.toString();
        if (!wordtrie.containsSubstring(word)) return;
        if (wordtrie.contains(word) && !validWord.contains(word) && level>2) validWord.add(word);

        int newx = 0;
        int newy = 0;
        VisitedChar newvchar = new VisitedChar(0, 0);
        for (int i = 0; i < 8; i++) {
            newx = vchar.x + xdis[i];
            newy = vchar.y + ydis[i];
            if (validIndex(newx, newy) && !visited[newy][newx]) {
                visited[newy][newx] = true;
                newvchar.x = newx;
                newvchar.y = newy;
                getValidWords(board, validWord, newvchar, substring, level+1);
                if (substring.length()>level) substring.delete(level, substring.length());
                visited[newy][newx] = false;
            }
        }
    }


    public int scoreOf(String word) {
        if (!wordtrie.contains(word)) return 0;
        int length = word.length();
        if (length<3) return 0;
        else if (length<5) return 1;
        else if (length==5) return 2;
        else if (length==6) return 3;
        else if (length==7) return 5;
        else return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
