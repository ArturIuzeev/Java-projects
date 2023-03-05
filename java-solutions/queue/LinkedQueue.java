package queue;

import java.util.Objects;
import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue {
    private List start;
    private List end;
    private List helper;

    private static class List {
        private final Object value;
        private List next;

        public List(Object value, List next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }

    @Override
    public Object dequeueImpl() {
        Object result = start.value;
        start = start.next;
        return result;
    }

    @Override
    public Object elementImpl() {
        return start.value;
    }

    @Override
    protected void clearImpl() {
        start = null;
        end = null;
    }

    @Override
    protected void enqueueImpl(Object element) {
        List el = end;
        end = new List(element, null);
        if (size == 0) {
            start = end;
        } else {
            el.next = end;
        }
        size++;
    }
}
