package com.othelle.samples.memcached;

/**
 * author: v.vlasov
 */
public interface Semaphore {
    boolean acquire();

    boolean release();

    int current();
}
