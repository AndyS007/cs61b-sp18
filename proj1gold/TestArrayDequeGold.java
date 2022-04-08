import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDequeGold {
    @Test
    public void testOne() {

        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> good = new ArrayDequeSolution<>();
        student.addFirst(5);
        student.addFirst(3);
        System.out.println(student.removeFirst());
        System.out.println(student.size());

    }
}
