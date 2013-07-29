package com.othelle.samples.memcached;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.IntegerTranscoder;
import net.spy.memcached.transcoders.Transcoder;

import java.util.concurrent.TimeUnit;

/**
 * author: v.vlasov
 */
public class MemcachedSemaphoreBasic implements Semaphore {

    //Note, this is NOT A REAL SEMAPHORE! We hard-code
    public static final int INITIAL_EXP = (int) TimeUnit.DAYS.toSeconds(20);
    public static final Transcoder<Integer> transcoder = new IntegerTranscoder();


    private MemcachedClient memcached;
    private String name;
    private int maxCapacity;
    private int retries = 8192;

    public MemcachedSemaphoreBasic(MemcachedClient memcached, String name, int maxCapacity) {
        this.memcached = memcached;
        this.name = name;
        this.maxCapacity = maxCapacity;
        memcached.add(name, INITIAL_EXP, 0); //if there is no semaphore key - create one
    }

    @Override
    public boolean acquire() {
        for (int i = 0; i < retries; i++) {
            CASValue<Integer> value = memcached.gets(name, transcoder);

            if (value.getValue() >= maxCapacity)
                return false;

            if (memcached.cas(name, value.getCas(), value.getValue() + 1, transcoder) == CASResponse.OK)
                return true;
        }

        return false;
    }

    @Override
    public boolean release() {
        for (int i = 0; i < retries; i++) {
            CASValue<Integer> value = memcached.gets(name, transcoder);

            if (value.getValue() <= 0)
                return false;

            if (memcached.cas(name, value.getCas(), value.getValue() - 1, transcoder) == CASResponse.OK)
                return true;
        }

        return false;
    }

    @Override
    public int current() {
        return memcached.get(name,transcoder);
    }


    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }
}
