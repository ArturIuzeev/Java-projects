package queue;
import java.util.*;
// Model: a[0]...a[n]
// Invariant: for i=1...n: a[i] != null
public class ArrayQueueModule {
    private static Object[] elements = new Object[2];
    private static int end = 0;
    private static int start = 0;
    private static int size;
    private static final Map<Object, Integer> hashmap = new HashMap<>();

    // Pred:true
    // Post:element != null, hashmap.contains(element), end' = (end + 1) % element.length, size' = size + 1;
    public static void enqueue(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        elements[end] = element;
        if (!hashmap.containsKey(elements[end])) {
            hashmap.put(elements[end], 1);
        } else {
            hashmap.put(elements[end], hashmap.get(elements[end]) + 1);
        }
        end = (end + 1) % elements.length;
        size++;
    }

    // Pred: true
    // Post: elements.length' = elements.length * 2 || elements.length' = elements.length
    private static void ensureCapacity() {
        if (start == (end + 1) % elements.length) {
            Object[] tmp = new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                tmp[i] = elements[(i + start) % elements.length];
            }
            end = elements.length - 1;
            elements = tmp;
            start = 0;
        }
    }

    // Pred: 0 <= start < elements.length
    // Post: element[start] the first element in the queue
    public static Object element() {
        return elements[start];
    }

    // Pred: elements.length >= 1
    // Post: size' = size - 1, start' = (start + 1) % elements.length, elements'[start] = null
    public static Object dequeue() {
        assert size >= 1;
        Object result = elements[start];
        hashmap.put(elements[start], hashmap.get(elements[start]) - 1);
        elements[start] = null;
        start = (start + 1) % elements.length;
        size--;
        return result;
    }

    // Pred: size != null
    // Post: size' = size
    public static int size() {
        return size;
    }

    // Pred: size != null
    // Post: size' = size
    public static boolean isEmpty() {
        return size == 0;
    }

    // Post: true
    // Pred: elements' = null, hashmap' = null, start' = 0, end' = 0, size' = 0
    public static void clear() {
        Arrays.fill(elements, null);
        hashmap.clear();
        size = 0;
        end = 0;
        start = 0;
    }

    // Post: true
    // Pred: hashmap' = hashmap, !hashmap.contains(key) -> 0
    public static int count(Object key) {
        return hashmap.getOrDefault(key,0);
    }
}
