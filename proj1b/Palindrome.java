public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> result = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i += 1) {
            char character = word.charAt(i);
            result.addLast(character);
        }
        return result;
    }
    public Deque<Character> wordToReverseDeque(String word) {
        Deque<Character> result = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i += 1) {
            char character = word.charAt(i);
            result.addFirst(character);
        }
        return result;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> normalOrder = wordToDeque(word);
        Deque<Character> reverseOrder = wordToReverseDeque(word);
        return  normalOrder.equals(reverseOrder);
    }
    //oddMiddle = (word.length() - 1) /2
    //evenMiddle = word.length() /2
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> normalOrder = wordToDeque(word);
        Deque<Character> reverseOrder = wordToReverseDeque(word);
        int middle;
        middle = word.length() % 2 == 1? (word.length() - 1) / 2 : word.length() / 2;
        for (int i = 0; i < middle; i +=1 ) {
            if(!cc.equalChars(normalOrder.get(i), reverseOrder.get(i))) {
                return false;
            }
        }
        return true;
    }

}
