package com.othelle.samples.memcached;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.IntegerTranscoder;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * author: v.vlasov
 */
public class MemcachedSemaphoreBase {
    //Note, this is NOT A REAL SEMAPHORE! We hard-code a bit :)
    public static final int INITIAL_EXP = (int) TimeUnit.DAYS.toSeconds(20);
    public static final IntegerTranscoder TRANSCODER = new IntegerTranscoder();
    protected MemcachedClient memcached;
    private String name;
    private int capacity;
    protected int retries = 8192;

    public MemcachedSemaphoreBase(int capacity, MemcachedClient memcached, String name) {
        this.capacity = capacity;
        this.memcached = memcached;
        this.name = name;
    }

    @PostConstruct
    protected void init(MemcachedClient memcached, String name) {
        memcached.add(name, INITIAL_EXP, 0);//if there is no semaphore key - create one
        // Since the key is going to expire - update the expiration period. Not supported in ASCII protocol
        // memcached.touch(name, INITIAL_EXP);
    }

    public int current() {
        return memcached.get(getName(), TRANSCODER);
    }


    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }
}
