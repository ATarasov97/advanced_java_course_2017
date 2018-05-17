package edu.technopolis.advancedjava;

import java.util.concurrent.atomic.AtomicInteger;

public class DeadlockBarrier {
    private final Object LOCK = new Object();
    private int numOfThreads;
    private AtomicInteger counter = new AtomicInteger(0);

    public DeadlockBarrier(int numOfThreads) {
        this.numOfThreads = numOfThreads;
    }

    public void passBarrier() {
        counter.incrementAndGet();
        synchronized (LOCK) {
            while (counter.get() < numOfThreads) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LOCK.notifyAll();
        }
    }
}