package queue;

import java.util.*;
// Model: a[0]...a[n]
// Invariant: for i=1...n: a[i] != null

public class ArrayQueueADT {
    private Object[] elements = new Object[2];
    private int end;
    private int start;
    private int size;
    private Map<Object, Integer> hashmap = new HashMap<>();

    //Pred: true
    //Post: elements' = new object[2] &&  hashmap = new HashMap<>()
    public ArrayQueueADT() {}

    // Pred: true
    // Post: elements.length = 2 && hashmap = new HashMap<>()
    public static ArrayQueueADT create() {
        final ArrayQueueADT queue = new ArrayQueueADT();
        queue.elements = new Object[2];
        queue.hashmap = new HashMap<>();
        return queue;
    }

    // Pred:true
    // Post: queueADT.element != null, queueADT.hashmap.contains(element), queueADT.end' = (end + 1) % queueADT.element.length, queueADT.size' = queueADT.size + 1;
    public static void enqueue(final ArrayQueueADT queueADT, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queueADT);
        queueADT.elements[queueADT.end] = element;
        if (!queueADT.hashmap.containsKey(queueADT.elements[queueADT.end])) {
            queueADT.hashmap.put(queueADT.elements[queueADT.end], 1);
        } else {
            queueADT.hashmap.put(queueADT.elements[queueADT.end], queueADT.hashmap.get(queueADT.elements[queueADT.end]) + 1);
        }
        queueADT.end = (queueADT.end + 1) % queueADT.elements.length;
        queueADT.size++;
    }

    // Pred: true
    // Post: queueADT.elements.length' = queueADT.elements.length * 2 || queueADT.elements.length' = queueADT.elements.length
    private static void ensureCapacity(final ArrayQueueADT queueADT) {
        if (queueADT.start == (queueADT.end + 1) % queueADT.elements.length) {
            Object[] tmp = new Object[queueADT.elements.length * 2];
            for (int i = 0; i < queueADT.size; i++) {
                tmp[i] = queueADT.elements[(i + queueADT.start) % queueADT.elements.length];
            }
            queueADT.end = queueADT.elements.length - 1;
            queueADT.elements = tmp;
            queueADT.start = 0;
        }
    }

    // Pred: 0 <= queueADT.start < queueADT.elements.length
    // Post: queueADT.element[start] the first element in the queue
    public static Object element(final ArrayQueueADT queueADT) {
        return queueADT.elements[queueADT.start];
    }

    // Pred: queueADT.elements.length >= 1
    // Post: queueADT.size' = queueADT.size - 1, queueADT.start' = (queueADT.start + 1) % queueADT.elements.length, queueADT.elements'[queueADT.start] = null
    public static Object dequeue(final ArrayQueueADT queueADT) {
        assert queueADT.size >= 1;
        Object result = queueADT.elements[queueADT.start];
        queueADT.hashmap.put(queueADT.elements[queueADT.start], queueADT.hashmap.get(queueADT.elements[queueADT.start]) - 1);
        queueADT.elements[queueADT.start] = null;
        queueADT.start = (queueADT.start + 1) % queueADT.elements.length;
        queueADT.size--;
        return result;
    }

    // Pred: queueADT.size != null
    // Post: queueADT.size' = queueADT.size
    public static int size(final ArrayQueueADT queueADT) {
        return queueADT.size;
    }

    // Pred: queueADT.size != null
    // Post: queueADT.size' = queueADT.size
    public static boolean isEmpty(final ArrayQueueADT queueADT) {
        return queueADT.size == 0;
    }

    // Post: true
    // Pred: queueADT.elements' = null, queueADT.hashmap' = null, queueADT.start' = 0, queueADT.end' = 0, queueADT.size' = 0
    public static void clear(final ArrayQueueADT queueADT) {
        Arrays.fill(queueADT.elements, null);
        queueADT.hashmap.clear();
        queueADT.size = 0;
        queueADT.end = 0;
        queueADT.start = 0;
    }

    // Post: true
    // Pred: queueADT.hashmap' = queueADT.hashmap, !hqueueADT.ashmap.contains(key) -> 0
    public static int count(final ArrayQueueADT queueADT, Object key) {
        return queueADT.hashmap.getOrDefault(key, 0);
    }
}