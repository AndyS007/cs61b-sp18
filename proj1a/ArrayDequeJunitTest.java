import org.junit.Test;

public class ArrayDequeJunitTest {
    @Test
    public void testAddFirstAndLast() {
        Deque<Integer> arrayDeque= new ArrayDeque<>();
        for(int i = 0; i < 60; i++) {
            arrayDeque.addLast(i);
        }
        arrayDeque.printDeque();


    }
    @Test
    //to be finished
    public void testPrintDeque() {


    }
}
