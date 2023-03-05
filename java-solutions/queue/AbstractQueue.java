package queue;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size;

    public int countIf(Predicate<Object> t) {
        int count = 0;
        Object helper;
        for (int i = 0; i < size; i++) {
            helper = dequeue();
            if (t.test(helper)) {
                count++;
            }
            enqueue(helper);
        }
        return count;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object element() {
        assert size > 0;
        return elementImpl();
    }

    protected abstract Object elementImpl();

    public void clear() {
        size = 0;
        clearImpl();
    }

    protected abstract void clearImpl();

    protected abstract void enqueueImpl(Object element);

    public void enqueue(final Object element) {
        Objects.requireNonNull(element);
        enqueueImpl(element);
    }

    public Object dequeue() {
        assert size > 0;
        size--;
        return dequeueImpl();
    }

    protected abstract Object dequeueImpl();
}
