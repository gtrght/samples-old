package com.othelle.samples.memcached;

import net.spy.memcached.CASMutation;
import net.spy.memcached.CASMutator;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.IntegerTranscoder;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * author: v.vlasov
 */
public class MemcachedSemaphoreSpyWay implements Semaphore {

    //Note, this is NOT A REAL SEMAPHORE! We hard-code a bit :)
    public static final int INITIAL_EXP = (int) TimeUnit.DAYS.toSeconds(20);
    public static final IntegerTranscoder TRANSCODER = new IntegerTranscoder();

    private CASMutation<Integer> incrementMutation = new CASMutation<Integer>() {
        @Override
        public Integer getNewValue(Integer current) {
            if (capacity < current)
                throw new RuntimeException("Unable to acquire");

            return current + 1;
        }
    };

    private CASMutation<Integer> decrementMutation = new CASMutation<Integer>() {
        @Override
        public Integer getNewValue(Integer current) {
            if (current <= 0)
                throw new RuntimeException("Unable to acquire");

            return current - 1;
        }
    };
    private MemcachedClient memcached;
    private String name;
    private int capacity;
    private CASMutator<Integer> mutator;

    public MemcachedSemaphoreSpyWay(MemcachedClient memcached, String name, int capacity) {
        this.memcached = memcached;
        this.name = name;
        this.capacity = capacity;

        mutator = new CASMutator<Integer>(memcached, new IntegerTranscoder());
        //Usually you don't want to put such a code in your constructor. That's a bad idea.
        //It's better to use @PostConstruct annotation and an appropriate IoC container.
        init(memcached, name);
    }

    @PostConstruct
    protected void init(MemcachedClient memcached, String name) {
        memcached.add(name, INITIAL_EXP, 0);//if there is no semaphore key - create one
        // Since the key is going to expire - update the expiration period. Not supported in ASCII protocol
        // memcached.touch(name, INITIAL_EXP);
    }


    @Override
    public boolean acquire() {
        try {
            mutator.cas(name, 0, INITIAL_EXP, incrementMutation);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        try {
            mutator.cas(name, 0, INITIAL_EXP, decrementMutation);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int current() {
        return memcached.get(name, TRANSCODER);
    }

}
