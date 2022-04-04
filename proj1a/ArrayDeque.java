public class ArrayDeque<T> {
    private int nextFirst;
    private int nextLast;
    private int size;
    private T[] items;

    public ArrayDeque() {
        items = (T[]) new Object[100];
        size = 0;
        nextFirst = items.length / 2;
        nextLast = nextFirst+ 1;

    }
    public void addFirst(T item) {
        items[nextFirst] = item;
        nextFirst = (nextFirst + items.length - 1) % items.length;
        size += 1;

    }

    public void addLast(T item) {
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size += 1;

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for(int i = nextFirst + 1, counter = 0; counter < size; i++, counter++) {
            int index = i % items.length;
            System.out.print(items[index] + " ");
        }
        System.out.println();

    }

    public T removeFirst() {
        return null;
    }

    public T removeLast() {
        return null;
    }
    //need to fix!!!!
    public T get(int index) {
        return items[nextFirst + 1];
    }
}
