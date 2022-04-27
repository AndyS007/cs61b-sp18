package lab11.graphs;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private int s;
    private boolean cycleFound = false;

    private class Node {
        int v;
        int parent;
        private Node(int v, int parent) {
            this.v = v;
            this.parent = parent;

        }

    }

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        distTo[0] = 0;


    }

    @Override
    public void solve() {
        // TODO: Your code here!
        dfs(new Node(0, 0));

    }

    //private Stack<Integer> stack;
    private int overlapPoint;
    private boolean finished = false;
    // Helper methods go here
    private void dfs(Node node) {
        marked[node.v] = true;
        announce();

        for (int w : maze.adj(node.v)) {
            if (cycleFound) {
                break;
            }
            if (marked[w] && w != node.parent) {
                cycleFound = true;
                overlapPoint = w;
                edgeTo[w] = node.v;
                announce();
                break;

            }
            if (!marked[w]) {
                distTo[w] = distTo[node.v] + 1;
                dfs(new Node(w, node.v));
            }
        }
        if (cycleFound && !finished) {
            edgeTo[node.v] = node.parent;
            announce();
            if (node.parent == overlapPoint) {
                finished = true;
            }
        }
    }



}

