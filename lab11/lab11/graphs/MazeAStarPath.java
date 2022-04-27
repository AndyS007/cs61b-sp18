package lab11.graphs;

import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private PriorityQueue<Node> pq;

    private class Node implements Comparable<Node> {
        int vertice;
        int distance;
        Node (int v, int p) {
            vertice = v;
            distance = p;
        }


        @Override
        public int compareTo(Node o) {
            return this.distance - o.distance;
        }
    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }


    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        // TODO
        pq = new PriorityQueue<>();
        pq.add(new Node(s, (distTo[0]) + h(0)));
        while (!pq.isEmpty()) {
            Node tmp = pq.remove();
            marked[tmp.vertice] = true;
            announce();
            if (tmp.vertice == t) {
                targetFound = true;
                return;
            }
            for (int w : maze.adj(tmp.vertice)) {
                if (!marked[w]) {
                    edgeTo[w] = tmp.vertice;
                    distTo[w] = distTo[tmp.vertice] + 1;
                    announce();
                    pq.add(new Node(w, distTo[w] + h(w)));
                }


            }

        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

