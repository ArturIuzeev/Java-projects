package info.kgeorgiy.ja.iuzeev.arrayset;

import java.util.AbstractList;
import java.util.List;

public class ReverseArrayList<E> extends AbstractList<E> {

    private final List<E> list;
    public ReverseArrayList(List<E> list) {
        this.list = list;
    }

    @Override
    public E get(int index) {
        return list.get(list.size() - index - 1);
    }

    public List<E> getList() {
        return list;
    }

    @Override
    public int size() {
        return this.list.size();
    }
}
