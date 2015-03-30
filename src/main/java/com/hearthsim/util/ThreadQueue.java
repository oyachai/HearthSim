package com.hearthsim.util;

import java.util.LinkedList;

public class ThreadQueue {
    private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList<Runnable> queue;

    public ThreadQueue(int nThreads) {
        this.nThreads = nThreads;
        queue = new LinkedList<>();
        threads = new PoolWorker[nThreads];
    }

    public void queue(Runnable r) {
        queue.addLast(r);
    }

    public void runQueue() throws InterruptedException {
        for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
        for (int i = 0; i < nThreads; ++i) {
            threads[i].join();
        }
    }

    private class PoolWorker extends Thread {

        @Override
        public void run() {
            Runnable r;
            while (true) {
                synchronized(queue) {
                    if (queue.isEmpty()) {
                        break;
                    }
                    r = queue.removeFirst();
                }

                // If we don't catch RuntimeException,
                // the pool could leak threads
                try {
                    r.run();
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
