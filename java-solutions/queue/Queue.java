package queue;

// Model: a[0]...a[n]
// Invariant: for i=1...n: a[i] != null

import java.util.function.Predicate;

public interface Queue {

    // Pred: true
    // Post: size' = size && count != null && count_of_dequeue == count_of_enqueue
    int countIf(Predicate<Object> t);

    // Pred: true
    // Post: element' != null && size' = size + 1 && elements[end'] = element
    void enqueue(Object element);

    // Pred: elements.length > 0
    // Post: size' = size - 1, start' = (start + 1) % elements.length, elements'[start] = null
    Object dequeue();

    // Pred: 0 <= start < elements.length
    // Post: element[start] the first element in the queue
    Object element();

    // Pred: 0 <= size < max.integer
    // Post: size' = size
    int size();

    //Pred: 0 <= size < max.integer
    //Post: size' = size
    boolean isEmpty();
}
