package ru.nsu.fit.g14203.popov.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SingleThreadPool extends ThreadPoolExecutor {

    private static class SingleEntryBlockingQueue extends ArrayBlockingQueue<Runnable> {

        SingleEntryBlockingQueue() {
            super(1);
        }

        @Override
        public boolean offer(Runnable runnable) {
            if (!this.isEmpty())
                poll();
            return super.offer(runnable);
        }
    };

    public SingleThreadPool() {
        super(1, 1,
              1, TimeUnit.DAYS,
              new SingleEntryBlockingQueue());
    }
}
