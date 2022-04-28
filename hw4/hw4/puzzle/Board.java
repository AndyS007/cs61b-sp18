package hw4.puzzle;

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
    int[][] tiles;
    int size;
    public Board(int[][] tiles) {
        this.tiles = tiles;
        size = tiles.length;

    }
    public int tileAt(int i, int j) {
        return tiles[i][j];

    }
    public int size() {
        return size;

    }
    public Iterable<WorldState> neighbors() {
        return null;

    }
    public int hamming() {
        return 0;

    }
    public int manhattan() {

        return 0;
    }
    public int estimatedDistanceToGoal() {

        return 0;
    }
    public boolean equals(Object y) {
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
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
