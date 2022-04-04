public class ArrayDeque<T> implements Deque<T>{
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
    @Override
    public void addFirst(T item) {
        items[nextFirst] = item;
        nextFirst = (nextFirst + items.length - 1) % items.length;
        size += 1;

    }

    @Override
    public void addLast(T item) {
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size += 1;

    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for(int i = nextFirst + 1, counter = 0; counter < size; i++, counter++) {
            int index = i % items.length;
            System.out.print(items[index] + " ");
        }
        System.out.println();

    }

    @Override
    public T removeFirst() {
        return null;
    }

    @Override
    public T removeLast() {
        return null;
    }
    //need to fix!!!!
    @Override
    public T get(int index) {
        return items[nextFirst + 1];
    }
}
