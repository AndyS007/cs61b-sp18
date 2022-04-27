package hw2;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestPercolation {
    int N = 10;
    Percolation p = new Percolation(N);
    @Test
    public void xyTo1DTest(){
    }
    @Test
    public void initailizeTest() {
        for (int i = 1; i <= N; i++){
        }
    }
    @Test
    public void openTest() {
        p.open(-1, 5);
    }

}
