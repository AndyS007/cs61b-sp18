import org.junit.Test;


import static org.junit.Assert.*;
public class LinkedListDequeJunitTest {
    @Test(timeout = 1000)
    public void testEquals(){
        LinkedListDeque<Integer> listOne = new LinkedListDeque<>();
        LinkedListDeque<Integer> listTwo = new LinkedListDeque<>();
        listOne.addFirst(2);
        listOne.addFirst(3);
        listOne.addFirst(4);

        listTwo.addLast(4);
        listTwo.addLast(3);
        listTwo.addLast(2);
        listOne.printDeque();
        listTwo.printDeque();
        assertEquals(listOne, listTwo);

    }
    @Test
    public void testEqualsNull(){
        LinkedListDeque<Integer> listOne = new LinkedListDeque<>();
        LinkedListDeque<Integer> listTwo = new LinkedListDeque<>();

        assertEquals(listOne, listTwo);
    }

    @Test
    public void testListOf() {
        LinkedListDeque<Integer> a = LinkedListDeque.of(1, 2, 3, 4, 5);
        LinkedListDeque<Integer> b = LinkedListDeque.of();
        LinkedListDeque<String> string = LinkedListDeque.of("hello", "world", "!");
        assertEquals(true, b.isEmpty());
        assertEquals(0, b.size());
        assertEquals(3, string.size());
    }

    @Test
    public void testRemoveFirstAndLast() {
        LinkedListDeque<Integer> a = LinkedListDeque.of(1, 2, 3 ,4, 5);
        a.removeFirst();
        a.removeLast();
        a.printDeque();
        LinkedListDeque<Integer> exp = LinkedListDeque.of(2, 3, 4);
        assertEquals(a, exp);
    }

    @Test
    public void testGet() {
        LinkedListDeque<Integer> a = LinkedListDeque.of(1, 2, 3 ,4, 5);
        assertEquals(Integer.valueOf(1), a.get(0));
        assertEquals(Integer.valueOf(2), a.get(1));
        assertEquals(Integer.valueOf(4), a.get(3));
        assertEquals(Integer.valueOf(5), a.get(4));
        a.printDeque();

    }
}
