package synthesizer;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer(4);
        for(int i = 0; i < arb.capacity(); i++ ){
             arb.enqueue(i);
        }
        for(int i = 0; i < arb.capacity(); i++ ){
            System.out.println(arb.dequeue());
        }
        for(int i = 4; i < 4 + arb.capacity(); i++ ){
            arb.enqueue(i);
        }
        assertEquals(Integer.valueOf(4), arb.dequeue());






    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
