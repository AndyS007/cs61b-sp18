package hw4.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Solver {
    PriorityQueue<SearchNode> pq;
    List<WorldState> list;
    int totalMoves;


    private class SearchNode implements Comparable<SearchNode> {
        WorldState word;
        int moves;
        SearchNode previousNode;
        public SearchNode(WorldState word, int moves, SearchNode previousNode) {
            this.word = word;
            this.moves = moves;
            this.previousNode = previousNode;
        }

        @Override
        public int compareTo(SearchNode o) {
            return this.moves + this.word.estimatedDistanceToGoal() - (o.moves + o.word.estimatedDistanceToGoal());
        }
    }
    public Solver(WorldState initial) {
        pq = new PriorityQueue<>();
        list = new ArrayList<>();
        totalMoves = 0;
        pq.add(new SearchNode(initial, totalMoves, null));
        while (!pq.isEmpty()) {
            SearchNode node = pq.remove();
            list.add(node.word);
            totalMoves += 1;
            if (node.word.isGoal()) {
                totalMoves = node.moves;
                list.remove(totalMoves);
            }
            for (WorldState ws : node.word.neighbors()) {
                if (node.previousNode == null || !ws.equals(node.previousNode.word)) {
                    pq.add(new SearchNode(ws, totalMoves, node));
                }

            }
        }


    }
    public int moves() {
        return totalMoves;

    }
    public Iterable<WorldState> solution() {
        return list;

    }
}
