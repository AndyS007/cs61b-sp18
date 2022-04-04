public class ArrayDeque<T> {
    private int nextFirst;
    private int nextLast;
    private int size;
    private T[] items;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;

    }
    public void addFirst(T item) {
        if (nextFirst == nextLast) {
            bigger();
        }
        items[nextFirst] = item;
        nextFirst = (nextFirst + items.length - 1) % items.length;
        size += 1;

    }

    public void addLast(T item) {
        if (nextFirst == nextLast) {
            bigger();
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size += 1;

    }
    private void bigger() {
        T[] resizedArray = (T[]) new Object[items.length * 2];
        int firstIndex = (nextFirst + 1) % items.length;
        if (nextFirst < nextLast) {
            System.arraycopy(items, firstIndex, resizedArray, 1, size);
        } else {
            System.arraycopy(items, firstIndex, resizedArray, 1, items.length - firstIndex);
            System.arraycopy(items, 0, resizedArray, items.length - firstIndex + 1, nextLast);
        }
        items = resizedArray;
        nextFirst = 0;
        nextLast = size + 1;


    }
    private void smaller() {
        T[] resizedArray = (T[]) new Object[items.length / 2];
        int firstIndex = (nextFirst + 1) % items.length;
        if (nextFirst < nextLast) {
            System.arraycopy(items, firstIndex, resizedArray, 1, size);
        } else {
            System.arraycopy(items, firstIndex, resizedArray, 1, items.length - firstIndex);
            System.arraycopy(items, 0, resizedArray, items.length - firstIndex + 1, nextLast);
        }
        items = resizedArray;
        nextFirst = 0;
        nextLast = size + 1;


    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size(); i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();

    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int preIndex = (nextFirst + 1) % items.length;
        T result = get(0);
        items[preIndex] = null;
        nextFirst = preIndex;
        size -= 1;
        if (lowUsageFactor() && items.length >= 16) {
            smaller();
        }
        return result;
    }

    public T removeLast() {

        if (isEmpty()) {
            return null;
        }
        int preIndex = (nextLast + items.length - 1) % items.length;
        T result = get(size - 1);
        items[preIndex] = null;
        nextLast = preIndex;
        size -= 1;

        if (lowUsageFactor() && items.length >= 16) {
            smaller();
        }
        return result;
    }
    private boolean lowUsageFactor() {
        double usageFactor =  (double) size / items.length;
        return usageFactor < 0.25;
    }
    public T get(int index) {
        if (isEmpty() || index >= size) {
            System.out.println("Error! Index out of bound! ");
            return null;
        }
        int returnIndex = 0;
        returnIndex = (nextFirst + 1 + index) % items.length;
        return items[returnIndex];
    }

    public static <K> ArrayDeque<K> of(K... args) {

        ArrayDeque<K> result = new ArrayDeque<>();
        for (K arg : args) {
            result.addLast(arg);
        }
        return result;
    }
}
