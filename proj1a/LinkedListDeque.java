
public class LinkedListDeque<T> {
    //inner class
    private class ListNode {

        T item;
        ListNode prev;
        ListNode next;

        ListNode(T item, ListNode prev, ListNode next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }

        ListNode() {

        }
    }

    private int size;
    private ListNode sentinel;

    public LinkedListDeque() {
        size = 0;
        sentinel = new ListNode();
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public void addFirst(T item) {
        ListNode temp = sentinel.next;
        sentinel.next = new ListNode(item, sentinel, temp);
        temp.prev = sentinel.next;
        size += 1;
    }

    public void addLast(T item) {
        ListNode temp = sentinel.prev;
        sentinel.prev = new ListNode(item, temp, sentinel);
        temp.next = sentinel.prev;
        size += 1;


    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        ListNode temp = sentinel.next;
        while (temp != sentinel) {
            System.out.print(temp.item + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        ListNode temp = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = temp.prev;
        size -= 1;
        return temp.item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        ListNode temp = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = temp.next;
        size -= 1;
        return temp.item;
    }
    public T get(int index) {
        ListNode temp = sentinel;
        if (index >= size) {
            return null;
        }
        if (index <= size / 2) {
            for (int i = -1; i < index; i += 1) {
                temp = temp.next;
            }
        } else {
            for (int i = size; i > index; i -= 1) {
                temp = temp.prev;
            }
        }
        return temp.item;
    }
    public boolean equals(Object x) {
        if (!(x instanceof LinkedListDeque)) {
            return false;
        }
        LinkedListDeque<T> list = (LinkedListDeque<T>) x;
        ListNode a = this.sentinel.next;
        ListNode b = list.sentinel.next;
        while (a != this.sentinel && b != list.sentinel) {
            if (a.item != b.item) {
                return false;
            }
            a = a.next;
            b = b.next;
        }
        return a == this.sentinel && b == list.sentinel;
    }

    public static <K> LinkedListDeque<K> of(K... args) {
        LinkedListDeque<K> result = new LinkedListDeque<>();
        for (K arg : args) {
            result.addLast(arg);
        }
        return result;
    }

}

