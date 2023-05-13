package info.kgeorgiy.ja.iuzeev.crawler;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

class Page {
    private final Queue<Runnable> queue;
    private final ExecutorService downloaders;
    private int count;
    private final int perHost;

    public Page(ExecutorService downloaders, int perHost) {
        this.downloaders = downloaders;
        this.count = 0;
        this.perHost = perHost;
        this.queue = new ArrayDeque<>();
    }

    public synchronized void addTask(final Runnable task) {
        if (count < perHost) {
            ++count;
            downloaders.submit(task);
        } else {
            queue.add(task);
        }
    }

    public synchronized void next() {
        final Runnable task = queue.poll();
        if (task == null) {
            --count;
        } else {
            downloaders.submit(task);
        }
    }
}
