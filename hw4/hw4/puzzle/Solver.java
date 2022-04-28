package hw4.puzzle;

import java.util.*;

public class Solver {
    PriorityQueue<SearchNode> pq;
    List<WorldState> list;


    private class SearchNode implements Comparable<SearchNode> {
        WorldState word;
        int moves;
        int priority;
        SearchNode previous;
        public SearchNode(WorldState word, int moves, SearchNode previous) {
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
        pq = new PriorityQueue<>();
        list = new ArrayList<>();
        pq.add(new SearchNode(initial, 0, null));
        while (!pq.peek().word.isGoal()) {
            SearchNode node = pq.remove();
            for (WorldState ws : node.word.neighbors()) {
                if (node.previous == null || !ws.equals(node.previous.word)) {
                    pq.add(new SearchNode(ws, node.moves + 1, node));
                }

            }
        }


    }
    public int moves() {
        return pq.peek().moves;

    }
    public Iterable<WorldState> solution() {
        Deque<WorldState> stack = new LinkedList<>();
        SearchNode tmp = pq.peek();
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
