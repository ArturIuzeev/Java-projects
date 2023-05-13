package info.kgeorgiy.ja.iuzeev.concurrent;

@FunctionalInterface
public interface MyCallable<V> {
    V call();
}
