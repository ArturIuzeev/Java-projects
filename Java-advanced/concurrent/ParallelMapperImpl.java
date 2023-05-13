package info.kgeorgiy.ja.iuzeev.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

public class ParallelMapperImpl implements ParallelMapper {
    private final Queue<Task<?>> queue;
    private final List<Thread> threads;

    public ParallelMapperImpl(int threadsCount) {
        queue = new ArrayDeque<>();
        threads = new ArrayList<>();

        for (int i = 0; i < threadsCount; i++) {
            Thread thread = new Thread(this::calc);
            threads.add(thread);
            thread.start();
        }
    }

    private void calc() {
        Task<?> task;

        while (true) {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                task = queue.poll();
            }
            task.run();
        }
    }

    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args) throws InterruptedException {
        List<Task<R>> tasks = new ArrayList<>();

        for (int i = 0; i < args.size(); i++) {
            final int p = i;
            tasks.add(new Task<>(() -> f.apply(args.get(p))));

            synchronized (queue) {
                queue.add(tasks.get(p));
                // :NOTE: notify
                queue.notify();
            }
        }

        List<R> result = new ArrayList<>();
        for (Task<R> task : tasks) {
            result.add(task.join());
        }

        return result;
    }

    @Override
    public void close() {
        for (Thread thread : threads) {
            thread.interrupt();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}