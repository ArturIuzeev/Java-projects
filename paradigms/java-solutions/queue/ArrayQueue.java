package queue;

import java.util.*;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue {
    private Object[] elements;
    private int end;
    private int start;
    private Map<Object, Integer> hashmap;

    public ArrayQueue() {
        this.elements = new Object[2];
        this.hashmap = new HashMap<>();
    }

    @Override
    public void enqueueImpl(final Object element) {
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

    private void ensureCapacity() {
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


    @Override
    public Object elementImpl() {
        return elements[start];
    }

    @Override
    public Object dequeueImpl() {
        Object result = elements[start];
        hashmap.put(elements[start], hashmap.get(elements[start]) - 1);
        elements[start] = null;
        start = (start + 1) % elements.length;
        return result;
    }

    @Override
    protected void clearImpl() {
        Arrays.fill(elements, null);
        hashmap.clear();
        size = 0;
        end = 0;
        start = 0;
    }

    public int count(Object key) {
        return hashmap.getOrDefault(key,0);
    }
}
