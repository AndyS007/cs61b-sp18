import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestOffByN {
    @Test
    public void testOffByN() {
        CharacterComparator offByN = new OffByN(1);
        assertTrue(offByN.equalChars('a', 'b'));
        assertTrue(offByN.equalChars('&', '%'));
        assertFalse(offByN.equalChars('i', 'b'));
    }

    @Test
    public void testOffBy5() {
        CharacterComparator offBy5 = new OffByN(5);
        assertTrue(offBy5.equalChars('a', 'f'));
        assertTrue(offBy5.equalChars('f', 'a'));
        assertFalse(offBy5.equalChars('i', 'b'));
    }
}
