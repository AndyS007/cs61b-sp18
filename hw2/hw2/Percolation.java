package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    // change to public when everything done.
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufTop;

    private int numberOfOpenSites;
    private int visualTopPoint;
    private int visualBottomPoint;
    private int N;
    public static void main(String[] args) {
        Percolation p = new Percolation(5);

    }

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must > 0");
        }
        this.N = N;
        grid = new boolean[N][N];
        uf = new WeightedQuickUnionUF(N * N + 2);
        ufTop = new WeightedQuickUnionUF(N * N + 1);
        visualTopPoint = N * N;
        visualBottomPoint = N * N + 1;
        numberOfOpenSites = 0;

    }
    public void open(int row, int col) {
        if (!inTheRange(row, col)) {
            throw new IndexOutOfBoundsException("Arguments out of scope");
        }
        if (grid[row][col]) {
            return;
        }
        grid[row][col] = true;
        numberOfOpenSites++;
        if (row == 0) {
            uf.union(visualTopPoint, xyTo1D(row, col));
            ufTop.union(visualTopPoint, xyTo1D(row, col));
        }
        if (row == N - 1) {
            uf.union(visualBottomPoint, xyTo1D(row, col));
        }
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[]dir : directions) {
            int x = row + dir[0];
            int y = col + dir[1];
            if (inTheRange(x, y) && grid[x][y]) {
                uf.union(xyTo1D(x, y), xyTo1D(row, col));
                ufTop.union(xyTo1D(x, y), xyTo1D(row, col));
            }
        }

    }
    private boolean inTheRange(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;

    }
    public boolean isOpen(int row, int col) {
        if (!inTheRange(row, col)) {
            throw new IllegalArgumentException("Arguments out of scope");
        }
        return grid[row][col];

    }
    public boolean isFull(int row, int col) {
        if (!inTheRange(row, col)) {
            throw new IllegalArgumentException("Arguments out of scope");
        }
        int num = xyTo1D(row, col);
        return isOpen(row, col) && ufTop.connected(visualTopPoint, num);
    }
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }
    private int xyTo1D(int x, int y) {
        return grid.length * x + y;
    }
    public boolean percolates() {
        return uf.connected(visualTopPoint, visualBottomPoint);

    }

}
