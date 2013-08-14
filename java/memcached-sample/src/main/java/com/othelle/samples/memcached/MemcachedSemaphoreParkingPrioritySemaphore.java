package com.othelle.samples.memcached;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * The implementation of reentrant semaphore
 * author: v.vlasov
 */
public class MemcachedSemaphoreParkingPrioritySemaphore extends MemcachedSemaphoreBase implements Semaphore {
    private final PriorityBlockingQueue<Thread> waitingThreads;

    public MemcachedSemaphoreParkingPrioritySemaphore(MemcachedClient memcached, String name, int maxCapacity) {
        super(maxCapacity, memcached, name);
        //Usually you don't want to put such a code in your constructor. That's a bad idea.
        //It's better to use @PostConstruct annotation and an appropriate IoC container.
        waitingThreads = new PriorityBlockingQueue<Thread>(getCapacity(), new Comparator<Thread>() {
            @Override
            public int compare(Thread o1, Thread o2) {
                return new Integer(o1.getPriority()).compareTo(o2.getPriority());
            }
        });
        init(memcached, name);
    }


    @Override
    public boolean acquire() {
        Thread currentThread = Thread.currentThread();
        waitingThreads.offer(currentThread);
        stateChanged();

        try {
            while (waitingThreads.peek() != currentThread || !acquire0()) {
                if (currentThread.isInterrupted())
                    return false;
                LockSupport.park();
            }
        } finally {
            waitingThreads.remove(currentThread);
            stateChanged();
        }

        return true;
    }

    private boolean acquire0() {
        int current = current();

        if (current >= getCapacity()) {
            return false;
        } else {
            CASValue<Integer> value = memcached.gets(getName(), TRANSCODER);
            return memcached.cas(getName(), value.getCas(), value.getValue() + 1, TRANSCODER) == CASResponse.OK;
        }
    }

    @Override
    public boolean release() {
        try {
            for (int i = 0; i < getRetries(); i++) {
                CASValue<Integer> value = memcached.gets(getName(), TRANSCODER);

                if (value.getValue() <= 0)
                    return false;

                if (memcached.cas(getName(), value.getCas(), value.getValue() - 1, TRANSCODER) == CASResponse.OK)
                    return true;
            }

            return false;
        } finally {
            stateChanged();
        }
    }

    private void stateChanged() {
        synchronized (waitingThreads) {
            waitingThreads.notifyAll();
        }
    }

    private void unparkWaitingThread() {
        //notify, try to unpark, in real life, this doesn't work when the lock is shared.
        // You should notify all the threads or react to the memcached value change:
        //  - Notification can be achieved by using JMS or Akka
        //  - Reacting to value changes can be done by using CouchDB (Membase) instead of Memcached.
        LockSupport.unpark(waitingThreads.peek());
    }

    @Override
    protected void init(MemcachedClient memcached, String name) {
        super.init(memcached, name);

        //starts heartbeat service to ensure that semaphore
        //this is not the code you want to use to notify your threads in production
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (waitingThreads.isEmpty()) {
                            synchronized (waitingThreads) {
                                try {
                                    waitingThreads.wait(1000);
                                } catch (InterruptedException e) {
                                    return;
                                }
                            }
                        } else {
                            Thread peek = waitingThreads.peek();
                            if (peek != null && peek.getState() != State.RUNNABLE && current() < getCapacity())
                                unparkWaitingThread();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}

