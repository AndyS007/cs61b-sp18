package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {

    /**
     * Board(tiles): Constructs a board from an N-by-N array of tiles where
     *               tiles[i][j] = tile at row i, column j
     * tileAt(i, j): Returns value of tile at row i, column j (or 0 if blank)
     * size():       Returns the board size N
     * neighbors():  Returns the neighbors of the current board
     * hamming():    Hamming estimate described below
     * manhattan():  Manhattan estimate described below
     * estimatedDistanceToGoal(): Estimated distance to goal. This method should
     *               simply return the results of manhattan() when submitted to
     *               Gradescope.
     * equals(y):    Returns true if this board's tile values are the same
     *               position as y's
     * toString():   Returns the string representation of the board. This
     *               method is provided in the skeleton
     */
    private int[][] start;
    private static final int BLANK = 0;
    private int size;

    public Board(int[][] tiles) {

        size = tiles.length;
        start = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                start[i][j] = tiles[i][j];
            }
        }

    }
    public int tileAt(int i, int j) {
        return start[i][j];

    }
    public int size() {
        return size;

    }
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int N = size();
        int blankRow = -1;
        int blankCol = -1;
        // find the BLANK
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) == BLANK) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }
        // copy the board
        int[][] aBoard = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                aBoard[i][j] = tileAt(i, j);
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (Math.abs(-blankRow + i) + Math.abs(j - blankCol) - 1 == 0) {
                    aBoard[blankRow][blankCol] = aBoard[i][j];
                    aBoard[i][j] = BLANK;
                    Board neighbor = new Board(aBoard);
                    neighbors.enqueue(neighbor);
                    aBoard[i][j] = aBoard[blankRow][blankCol];
                    aBoard[blankRow][blankCol] = BLANK;
                }
            }
        }
        return neighbors;

    }
    public int hamming() {
        int total = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                int val = tileAt(i, j);
                if (val == BLANK) {
                    continue;
                }
                if (toRow(val) != i || toCol(val) != j) {
                    total += 1;
                }

            }
        }

        return total;

    }
    public int manhattan() {
        int total = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                int tmp = tileAt(i, j);
                if (tmp == BLANK) {
                    continue;
                }
                if (toRow(tmp) != i || toCol(tmp) != j) {
                    total += Math.abs(i - toRow(tmp)) + Math.abs(j - toCol(tmp));

                }
            }
        }

        return total;
    }
    private int toRow(int n) {
        return (n - 1) / size();
    }
    private int toCol(int n) {
        return (n - 1) % size();

    }
    public int estimatedDistanceToGoal() {

        return manhattan();
    }
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Board other = (Board) o;
        if (other.size() != size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (this.tileAt(i, j) != other.tileAt(i, j)) {
                    return false;
                }
            }
        }

        return true;

    }
    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    public int hashCode() {
        return 0;
    }
}
