package info.kgeorgiy.ja.iuzeev.arrayset;

import java.util.*;
import java.util.stream.Collectors;

public class ArraySet<E> extends AbstractSet<E> implements NavigableSet<E> {
    private final List<E> list;
    private final Comparator<? super E> comparator;

    public ArraySet() {
        this.list = new ArrayList<>();
        this.comparator = null;
    }

    public ArraySet(Collection<? extends E> col) {
        List<E> l = new ArrayList<>(col);
        List<E> res = new ArrayList<>();
        if (!col.isEmpty()) {
            l.sort((Comparator<? super E>) Comparator.naturalOrder());
            for (E element : l) {
                if (res.size() != 0 && element != res.get(res.size() - 1)) {
                    res.add(element);
                } else if (res.size() == 0) {
                    res.add(element);
                }
            }
        }
        this.list = res;
        this.comparator = null;
//        List<E> l = new ArrayList<>(new TreeSet<>(col));
//        this.list = l;
//        this.comparator = null;

    }

    private ArraySet(List<E> l, Comparator<? super E> comp) {
        this.list = l;
        this.comparator = comp;
    }
    public ArraySet(Collection<? extends E> col, Comparator<? super E> comp) {
        TreeSet<E> treeSet = new TreeSet<>(comp);
        treeSet.addAll(col);
        this.list = treeSet.stream().toList();
        this.comparator = comp;
//        List<E> l = new ArrayList<>(col);
//        List<E> res = new ArrayList<>();
//        if (!l.isEmpty() && comp != null) {
//            l.sort(comp);
//            for (E element : l) {
//                if (res.size() != 0 && element != res.get(res.size() - 1)) {
//                    res.add(element);
//                } else if (res.size() == 0) {
//                    res.add(element);
//                }
//            }
//        } else if (!l.isEmpty()) {
//            l.sort((Comparator<? super E>) Comparator.naturalOrder());
//            for (E element : l) {
//                if (res.size() != 0 && element != res.get(res.size() - 1)) {
//                    res.add(element);
//                } else if (res.size() == 0) {
//                    res.add(element);
//                }
//            }
//        }
//        this.list = res;
//        this.comparator = comp;
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

    private int calcInd(E obj) {
        if (list.isEmpty()) {
            return -1;
        }
        return Collections.binarySearch(list, obj, comparator);
    }

    public int floorInd(E obj) {
        int index = calcInd(obj);
        return index < 0 ? -index - 2 : index;
    }

    public int ceilingInd(E obj) {
        int index = calcInd(obj);
        if (index < 0) {
            index = -index - 1;
            if (index > list.size()) {
                return -1;
            }
            return index;
        }
        return index;
    }

    public int higherInd(E obj) {
        int index = calcInd(obj);
        if (index < 0) {
            index = -index - 1;
            return index == list.size() ? -1 : index;
        }
        return index + 1 < list.size() ? index + 1 : -1;
    }

    public int lowerInd(E obj) {
        int index = calcInd(obj);
        if (index < 0) {
            index = -index - 2;
            return index;
        }
        return index != 0 ? index - 1 : -1;
    }

    private E getElement(int index) {
        return index < 0 ? null : list.get(index);
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
                return curr < list.size() && list.get(curr) != null;
            }

            @Override
            public E next() {
                return list.get(curr++);
            }
        };
    }

    private boolean flag = false;
    @Override
    public NavigableSet<E> descendingSet() {
        if (list.isEmpty()) {
            return new ArraySet<>(list, comparator);
        }
        List<E> reversed;
        if (list instanceof ReverseArrayList<E>) {
            reversed = ((ReverseArrayList<E>) list).getList();
            return new ArraySet<>(reversed, comparator);
        } else {
            reversed = new ReverseArrayList<>(list, true);
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

            private final int curr = list.size() - 1;

            @Override
            public boolean hasNext() {
                return curr > 0 && list.get(curr) != null;
            }

            @Override
            public E next() {
                return list.get(curr);
            }
        };
    }

    private int calcNavSetFromInclusive(E fromElement, boolean fromInclusive) {
        return fromInclusive ? ceilingInd(fromElement) : higherInd(fromElement);
    }

    private int calcNavSetToInclusive(E toElement, boolean toInclusive) {
        return toInclusive ? floorInd(toElement) : lowerInd(toElement);
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        if (list.isEmpty()) {
            return new ArraySet<>(Collections.emptyList(), comparator);
        }
        int from = calcNavSetFromInclusive(fromElement, fromInclusive);
        int to = calcNavSetToInclusive(toElement, toInclusive);

        if (from == -1 || to == -1) {
            return new ArraySet<>(Collections.emptyList(), comparator);
        }
        if (from > to) {
            throw new IllegalArgumentException();
        }

        return new ArraySet<>(list.subList(from, to + 1), comparator);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        if (list.isEmpty() || comparator == null) {
            return new ArraySet<>(Collections.emptyList(), comparator);
        }
        int index = calcNavSetToInclusive(toElement, inclusive);
        return index == -1 ? new ArraySet<>(Collections.emptyList(), comparator) : new ArraySet<>(list.subList(0, index + 1), comparator);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        if (list.isEmpty() || comparator == null) {
            return new ArraySet<>(Collections.emptyList(), comparator);
        }
        int index = calcNavSetFromInclusive(fromElement, inclusive);
        return index == -1 ? new ArraySet<>(Collections.emptyList(), comparator) : new ArraySet<>(list.subList(index, list.size()), comparator);
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public boolean contains(Object o) {
        return Collections.binarySearch(list, (E) o, comparator) >= 0;
    }

    public int calcSortSet(E Elem) {
        int index;
        if (comparator == null) {
            index = Collections.binarySearch(list, Elem, (Comparator<? super E>) Comparator.naturalOrder());
        } else {
            index = Collections.binarySearch(list, Elem, comparator);
        }
        return index < 0 ? -index - 1 : index;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        if (list.isEmpty()) {
            return new ArraySet<>(Collections.emptyList(), comparator);
        }
        return new ArraySet<>(list.subList(0, calcSortSet(toElement)), comparator);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        if (list.isEmpty()) {
            return new ArraySet<>(Collections.emptyList(), comparator);
        }
        return new ArraySet<>(list.subList(calcSortSet(fromElement), list.size()), comparator);
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
