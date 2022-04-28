package hw4.puzzle;


import edu.princeton.cs.algs4.MinPQ;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayList;
public class Solver {
    private MinPQ<SearchNode> pq;
    List<WorldState> list;


    private class SearchNode implements Comparable<SearchNode> {
        WorldState word;
        int moves;
        int priority;
        SearchNode previous;
       SearchNode(WorldState word, int moves, SearchNode previous) {
            this.word = word;
            this.moves = moves;
            this.priority = moves + word.estimatedDistanceToGoal();
            this.previous = previous;

        }

        @Override
        public int compareTo(SearchNode o) {
            return this.priority - o.priority;
        }
    }
    public Solver(WorldState initial) {
        pq = new MinPQ<>();
        list = new ArrayList<>();
        pq.insert(new SearchNode(initial, 0, null));
        while (!pq.min().word.isGoal()) {
            SearchNode node = pq.delMin();
            for (WorldState ws : node.word.neighbors()) {
                if (node.previous == null || !ws.equals(node.previous.word)) {
                    pq.insert(new SearchNode(ws, node.moves + 1, node));
                }

            }
        }


    }
    public int moves() {
        return pq.min().moves;

    }
    public Iterable<WorldState> solution() {
        Deque<WorldState> stack = new LinkedList<>();
        SearchNode tmp = pq.min();
        while (tmp != null) {
            stack.push(tmp.word);
            tmp = tmp.previous;
        }
        while (!stack.isEmpty()) {
            list.add(stack.pop());
        }

        return list;

    }
}
