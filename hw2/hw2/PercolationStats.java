package hw2;


import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private int TIMES;
    private Percolation per;
    private double[] threshold;
    private double mean;
    private double stddev;

    public static void main(String[] args) {
        int N = 20;
        int times = 100;
        PercolationFactory pf = new PercolationFactory();
        for (int i = 0; i < 5; i += 1) {
            Stopwatch timer = new Stopwatch();
            new PercolationStats(N, times, pf);
            double time = timer.elapsedTime();
            System.out.println(N + ": " + time);
            N *= 2;
        }

    }



    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        TIMES = T;
        threshold = new double[T];
        for (int i = 0; i < T; i++) {
            per = pf.make(N);
            while (!per.percolates()) {
                int x = StdRandom.uniform(N);
                int y = StdRandom.uniform(N);
                per.open(x, y);
            }
            threshold[i] = (double) per.numberOfOpenSites() / (N * N);

        }
        mean = mean();
        stddev = stddev();

    }
    public double mean() {
        double total = 0;
        for (double n : threshold) {
            total += n;
        }

        return total / TIMES;
    }
    public double stddev() {
        double total = 0;
        for (double n : threshold) {
            total += Math.pow((n - mean), 2);
        }
        return Math.sqrt(total / (TIMES - 1));

    }
    public double confidenceLow() {
        return mean - (1.96 * stddev / Math.sqrt(TIMES));

    }
    public double confidenceHigh() {
        return mean + (1.96 * stddev / Math.sqrt(TIMES));
    }

}
