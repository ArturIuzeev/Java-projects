package info.kgeorgiy.ja.iuzeev.arrayset;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

public class ReverseArrayList<E> extends AbstractList<E> implements RandomAccess {

    private final List<E> list;
    private boolean flag;
    public ReverseArrayList(List<E> list) {
        this.list = list;
    }

    public ReverseArrayList(List<E> list, boolean flag) {
        this.list = list;
        this.flag = flag;
    }

    @Override
    public E get(int index) {
        return flag ? list.get(list.size() - index - 1) : list.get(index);
    }

    public List<E> getList() {
        return list;
    }

    @Override
    public int size() {
        return this.list.size();
    }
}
