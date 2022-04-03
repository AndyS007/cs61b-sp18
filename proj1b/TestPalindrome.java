import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome("catac"));
        assertTrue(palindrome.isPalindrome("c"));
        assertTrue(palindrome.isPalindrome(""));
        assertFalse(palindrome.isPalindrome("cat"));
    }
    @Test
    public void testIsPalindromeCC() {
        CharacterComparator cc = new OffByOne();
        assertTrue(palindrome.isPalindrome("flake", cc));
        assertTrue(palindrome.isPalindrome("flke", cc));
        assertTrue(palindrome.isPalindrome("acdb", cc));
    }
    @Test
    public void testIsPalindromeOffByN() {
        CharacterComparator cc = new OffByN(5);
        assertTrue(palindrome.isPalindrome("afafaf", cc));
        assertTrue(palindrome.isPalindrome("af", cc));
        assertTrue(palindrome.isPalindrome("i", cc));
    }
}
