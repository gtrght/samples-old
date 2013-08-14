package com.othelle.samples.memcached;

/**
 * author: v.vlasov
 */
public interface SemaphoreWithTimeout extends Semaphore {
    public boolean acquire(long timeout);
}
