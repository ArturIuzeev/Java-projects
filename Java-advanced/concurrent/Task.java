package info.kgeorgiy.ja.iuzeev.concurrent;

class Task<R> {
    private boolean flag = false;
    private R result;
    private RuntimeException exception;

    private final MyCallable<R> operation;

    public Task(MyCallable<R> operation) {
        this.operation = operation;
    }

    public synchronized void run() {
        try {
            result = operation.call();
            flag = true;
            // :NOTE: точно так?
        } catch (RuntimeException e) {
            exception = e;
        } finally {
            notify();
        }
    }

    public synchronized R join() throws InterruptedException {
        if (exception != null) {
            throw exception;
        }

        while (!flag) {
            wait();
        }

        return result;
    }
}