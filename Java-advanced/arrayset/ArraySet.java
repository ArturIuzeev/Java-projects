package info.kgeorgiy.ja.iuzeev.arrayset;

import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements NavigableSet<E> {
    private final List<E> list;
    private final Comparator<? super E> comparator;

    public ArraySet() {
        this(Collections.emptyList(), null);
    }

    public ArraySet(Collection<? extends E> col) {
        this(col, null);
    }

    private ArraySet(List<E> l, Comparator<? super E> comp) {
        this.list = l;
        this.comparator = comp;
    }

    public ArraySet(Collection<? extends E> col, Comparator<? super E> comp) {
        Set<E> treeSet = new TreeSet<>(comp);
        treeSet.addAll(col);
        this.list = treeSet.stream().toList();
        this.comparator = comp;
    }


    @Override
    public E floor(E obj) {
        return getElement(floorInd(obj));
    }

    @Override
    public E ceiling(E obj) {
        return getElement(ceilingInd(obj));
    }

    @Override
    public E higher(E obj) {
        return getElement(higherInd(obj));
    }

    @Override
    public E lower(E obj) {
        return getElement(lowerInd(obj));
    }

    private int calcInd(E obj, int f, int s) {
        int index = Collections.binarySearch(list, obj, comparator);
        if (index < 0) {
            index = -index - 1 + s;
            return index;
        }
        return index + f;
    }

    public int ceilingInd(E obj) {
        return calcInd(obj, 0, 0);
    }

    public int higherInd(E obj) {
        return calcInd(obj, 1, 0);
    }

    public int floorInd(E obj) {
        return calcInd(obj, 0, -1);
    }

    public int lowerInd(E obj) {
        return calcInd(obj, -1, -1);
    }


    private E getElement(int index) {
        return index < 0 || index >= list.size() ? null : list.get(index);
    }

    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException("This operation is not available for this class");
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException("This operation is not available for this class");
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Unsupported operation");
            }

            private int curr = 0;

            @Override
            public boolean hasNext() {
                return curr < list.size();
            }

            @Override
            public E next() {
                return list.get(curr++);
            }
        };
    }

    @Override
    public NavigableSet<E> descendingSet() {
        if (list.isEmpty()) {
            return new ArraySet<>(Collections.emptyList(), comparator);
        }
        List<E> reversed;
        if (list instanceof ReverseArrayList<E> l) {
            reversed = l.getList();
            return new ArraySet<>(reversed, comparator);
        } else {
            reversed = new ReverseArrayList<>(list);
            return new ArraySet<>(reversed, Collections.reverseOrder(comparator));
        }
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<>() {
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Unsupported operation");
            }

            private int curr = list.size() - 1;

            @Override
            public boolean hasNext() {
                return curr > 0;
            }

            @Override
            public E next() {
                return list.get(curr--);
            }
        };
    }

    private int calcNavSetFromInclusive(E fromElement, boolean fromInclusive) {
        return fromInclusive ? ceilingInd(fromElement) : higherInd(fromElement);
    }

    private int calcNavSetToInclusive(E toElement, boolean toInclusive) {
        return toInclusive ? floorInd(toElement) : lowerInd(toElement);
    }

    public NavigableSet<E> NavSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        int from = calcNavSetFromInclusive(fromElement, fromInclusive);
        int to = calcNavSetToInclusive(toElement, toInclusive);
        if (from == -1 || to == -1 || from > to) {
            return new ArraySet<>(Collections.emptyList(), comparator);
        }
        return new ArraySet<>(list.subList(from, to + 1), comparator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        if (comparator != null && comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException("fromKey > toKey");
        } else if (comparator == null) {
            if (((Comparator<? super E>) Comparator.naturalOrder()).compare(fromElement, toElement) > 0) {
                throw new IllegalArgumentException("fromKey > toKey");
            }
        }
        return NavSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        if (list.isEmpty()) {
            return new ArraySet<>(Collections.emptyList(), comparator);
        }
        return NavSet(first(), true, toElement, inclusive);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        if (list.isEmpty()) {
            return new ArraySet<>(Collections.emptyList(), comparator);
        }
        return NavSet(fromElement, inclusive, last(), true);
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return Collections.binarySearch(list, (E) o, comparator) >= 0;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public E first() {
        if (!list.isEmpty()) {
            return list.get(0);
        }
        throw new NoSuchElementException("No such element on this position");
    }

    @Override
    public E last() {
        if (list.size() != 0) {
            return list.get(list.size() - 1);
        }
        throw new NoSuchElementException("No such element on this position");
    }

    @Override
    public int size() {
        return list.size();
    }
}
