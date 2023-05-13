package info.kgeorgiy.ja.iuzeev.concurrent;

import info.kgeorgiy.java.advanced.concurrent.AdvancedIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class IterativeParallelism implements AdvancedIP {
    private ParallelMapper mapper;

    public IterativeParallelism() {
    }

    public IterativeParallelism(ParallelMapper mapper) {
        this.mapper = mapper;
    }

    private <T, R> R calc(int threads,
                          List<T> values,
                          Function<List<T>, R> firstFunction,
                          Function<List<R>, R> secondFunction) throws InterruptedException {
        if (threads < 1) {
            throw new IllegalArgumentException("Not enough threads");
        }

        if (values.isEmpty()) {
            throw new IllegalArgumentException("No such values");
        }

        threads = Math.min(threads, values.size());
        int count = values.size() / threads;
        int remainder = values.size() % threads;

        List<Thread> threadList = new ArrayList<>();
        List<R> result = new ArrayList<>();

        List<List<T>> args = new ArrayList<>();

        int helper = 0;
        for (int i = 0; i < threads; i++) {

            if (mapper == null) {
                result.add(null);
            }

            final int position = i;
            final int left = helper;
            final int right;

            if (remainder > 0) {
                right = left + count + 1;
                --remainder;
            } else {
                right = left + count;
            }

            if (mapper != null) {
                args.add(values.subList(left, right));
            } else {
                threadList.add(new Thread(() -> result.set(position, firstFunction.apply(Collections.unmodifiableList(values.subList(left, right))))));
                threadList.get(i).start();
            }
            helper = right;

        }

        if (mapper != null) {
            return secondFunction.apply(mapper.map(firstFunction, args));
        } else {
            for (int i = 0; i < threadList.size(); i++) {
                try {
                    threadList.get(i).join();
                } catch (InterruptedException e) {
                    threadList.forEach(Thread::interrupt);

                    for (Thread thread : threadList) {
                        try {
                            thread.join();
                        } catch (InterruptedException ex) {
                            ex.addSuppressed(e);
                            throw ex;
                        }
                    }
                    throw e;
                }
            }
            return secondFunction.apply(result);
        }
    }

    @Override
    public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return Collections.unmodifiableList(
                calc(threads, values, x -> x.stream().filter(predicate).toList(), x -> x.stream().flatMap(Collection::stream).toList())
        );
    }

    @Override
    public <T, U> List<U> map(int threads, List<? extends T> values, Function<? super T, ? extends U> f) throws InterruptedException {
        return Collections.unmodifiableList(calc(threads, values, x -> x.stream().map(f).toList(), x -> x.stream().flatMap(Collection::stream).toList()));
    }

    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return calc(threads, values, x -> x.stream().max(comparator).orElse(null), x -> x.stream().max(comparator).orElse(null));
    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(threads, values, comparator.reversed());
    }

    @Override
    public String join(int threads, List<?> values) throws InterruptedException {
        return String.join("", map(threads, values, Object::toString));
    }

    @Override
    public <T> int count(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return filter(threads, values, predicate).size();
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return calc(threads, values, list -> list.stream().allMatch(predicate), list -> list.stream().allMatch(Boolean::booleanValue));

    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return !all(threads, values, predicate.negate());
    }

    @Override
    public <T> T reduce(int threads, List<T> values, Monoid<T> monoid) throws InterruptedException {
        return mapReduce(threads, values, Function.identity(), monoid);
    }

    @Override
    public <T, R> R mapReduce(int threads, List<T> values, Function<T, R> lift, Monoid<R> monoid) throws InterruptedException {
        return calc(threads, values, x -> x.stream().map(lift).reduce(monoid.getIdentity(), monoid.getOperator()), x -> x.stream().reduce(monoid.getIdentity(), monoid.getOperator()));
    }
}
