import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeJunitTest {
    @Test
    public void testAddFirstAndLast() {
        ArrayDeque<Integer> arrayDeque= new ArrayDeque<>();
        for(int i = 0; i < 60; i++) {
            arrayDeque.addLast(i);
        }
        arrayDeque.printDeque();


    }

    @Test
    public void testGet() {

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        deque.printDeque();
    }
    @Test
    public void testPrintDeque() {

        ArrayDeque<Integer> arrayDeque= new ArrayDeque<>();
        /*
        for(int i = 0; i < 50; i++) {
            arrayDeque.addLast(i);

        }
         */
        for(int i = 0; i < 100; i++) {
            arrayDeque.addFirst(i);
        }
        for(int i = 0; i < 95; i++) {
            arrayDeque.removeFirst();
        }
        arrayDeque.printDeque();


    }


    @Test
    public void testRemove() {

        ArrayDeque<Integer> arrayDeque= ArrayDeque.of(1, 2, 3, 4, 5);
        arrayDeque.removeFirst();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        assertEquals(arrayDeque.size(), 0);
        arrayDeque.printDeque();
    }

}
