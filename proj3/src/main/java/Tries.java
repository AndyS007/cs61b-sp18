import java.util.*;

public class Tries<Value> {
    private Node root;
    private static class Node {
        private Object val;
        private Map<Character, Node> next = new HashMap<>();
    }
    public static void main(String[] a) {
        Tries<Long> t = new Tries<>();
        t.put("same", 100L);
        t.put("sam", 100L);
        t.put("sa", 100L);
        t.put("saefs", 100L);
        for (String s : t.keyWithPrefix("sam")) {

            System.out.println(s);
        }
    }
    public Tries() {
    }
    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        Node x = get(root, key, 0);
        if (x == null) {
            return null;
        }
        return (Value) x.val;
    }
    private Node get(Node x, String key, int index) {
        if (x == null) {
            return null;
        }
        if (index == key.length()) {
            return x;
        }
        char c = key.charAt(index);
        return get(x.next.get(c), key, index + 1);
    }
    public boolean contains(String key) {
        return get(key) != null;
    }
    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        root = put(key, val, root, 0);
    }
    private Node put(String key, Value val, Node x, int index) {
        if (x == null) {
            x = new Node();
        }
        if (index == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(index);
        x.next.put(c, put(key, val, x.next.get(c), index + 1));
        return x;

    }
    public Iterable<String> keyWithPrefix(String prefix) {
        Queue<String> results = new LinkedList<>();
        collect(get(root, prefix, 0), prefix, results);
        return results;

    }
    private void collect(Node x, String prefix, Queue<String> q) {
        if (x == null) {
            return;
        }
        if (x.val != null) {
            q.add(prefix);
        }
        for (Map.Entry<Character, Node> entry : x.next.entrySet()) {
            collect(entry.getValue(), prefix + entry.getKey(), q);
        }


    }

}
