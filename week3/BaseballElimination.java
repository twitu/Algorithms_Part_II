import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {
    private int teamsNumber;
    private HashMap<String, Integer> teamId;
    private ArrayList<String> teamnames;
    private int[] wins;
    private int[] losses;
    private int[] left;
    private int[][] games;
    private FlowNetwork flowgraph;
    private FordFulkerson ford;
    private String currentteam;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        teamsNumber = in.readInt();
        wins = new int[teamsNumber];
        losses = new int[teamsNumber];
        left = new int[teamsNumber];
        games = new int[teamsNumber][teamsNumber];
        teamnames = new ArrayList<>(teamsNumber);
        teamId = new HashMap<>();

        for (int i=0; i<teamsNumber; i++) {
            teamnames.add(in.readString());
            teamId.put(teamnames.get(i), i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            left[i] = in.readInt();

            for (int j=0; j<teamsNumber; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return teamsNumber;
    }

    public Iterable<String> teams() {
        return teamnames;
    }

    public int wins(String team) {
        return wins[teamId.get(team)];
    }

    public int losses(String team) {
        return losses[teamId.get(team)];
    }

    public int remaining(String team) {
        return left[teamId.get(team)];
    }

    public int against(String team1, String team2) {
        return games[teamId.get(team1)][teamId.get(team2)];
    }

    private boolean isTriviallyEliminated(String team) {
        int id = teamId.get(team);
        int total = wins[id] + left[id];
        for (int i=0; i<teamsNumber; i++) {
            if (i==id) continue;
            if (wins[i] > total) return true;
        }
        return false;
    }

    private boolean validTeam(String team) {
        return teamId.containsKey(team);
    }

    public boolean isEliminated(String team) {
        if (!validTeam(team)) throw new IllegalArgumentException("Invalid team name.");
        currentteam = team;
        if (isTriviallyEliminated(team)) return true;
        flowgraph = new FlowNetwork(2 + teamsNumber*teamsNumber + teamsNumber);
        int square = teamsNumber*teamsNumber;
        int id = teamId.get(team);
        int i = 0;
        int count;
        for (i=0; i<teamsNumber; i++) {
            if (i==id) continue;

            for (count=i+1; count<teamsNumber; count++) {
                if (count==id) continue;
                if (games[i][count]!=0) {
                    flowgraph.addEdge(new FlowEdge(0, 1+i*teamsNumber+count, games[i][count]));
                    flowgraph.addEdge(new FlowEdge(1+i*teamsNumber+count, 1+square+i, Double.MAX_VALUE));
                    flowgraph.addEdge(new FlowEdge(1+i*teamsNumber+count, 1+square+count, Double.MAX_VALUE));
                }
            }
        }

        int total = wins[id] + left[id];
        for (i=0; i<teamsNumber; i++) {
            if (i==id) continue;
            if ((total - wins[i]) < 0) flowgraph.addEdge(new FlowEdge(1+square+teamsNumber, 1+square+i ,wins[i] - total));
            else flowgraph.addEdge(new FlowEdge(1+square+i, 1+square+teamsNumber, total - wins[i]));
        }
        ford = new FordFulkerson(flowgraph, 0, 1+teamsNumber*teamsNumber+teamsNumber);

        double max = ford.value();
        double sum = 0;
        for (i=0; i<teamsNumber; i++) {
            if (i==id) continue;
            for (int j=i+1; j<teamsNumber; j++) {
                if (j==id) continue;
                sum += games[i][j];
            }
        }
        return sum > max;
    }

    private Iterable<String> trivialCertificateOfElimination(String team) {
        ArrayList<String> names = new ArrayList<>();
        int id = teamId.get(team);
        int total = wins[id] + left[id];
        for (int i=0; i<teamsNumber; i++) {
            if (i==id) continue;
            if (wins[i] > total) names.add(teamnames.get(i));
        }
        return names;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!validTeam(team)) throw new IllegalArgumentException("Invalid team name.");
        if (isTriviallyEliminated(team)) {
            return trivialCertificateOfElimination(team);
        }

        ArrayList<String> names = new ArrayList<>();
        if (!isEliminated(team)) return null;
        int square = teamsNumber*teamsNumber;
        int id = teamId.get(team);
        for (int i=0; i<teamsNumber; i++) {
            if (i==id) continue;
            if (ford.inCut(i+square+1)) names.add(teamnames.get(i));
        }
        return names;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
