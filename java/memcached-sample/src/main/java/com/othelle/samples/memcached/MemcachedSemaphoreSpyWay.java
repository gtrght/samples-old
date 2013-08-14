package com.othelle.samples.memcached;

import net.spy.memcached.CASMutation;
import net.spy.memcached.CASMutator;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.IntegerTranscoder;

/**
 * author: v.vlasov
 */
public class MemcachedSemaphoreSpyWay extends MemcachedSemaphoreBase implements Semaphore {

    private CASMutation<Integer> incrementMutation = new CASMutation<Integer>() {
        @Override
        public Integer getNewValue(Integer current) {
            if (getCapacity() < current)
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
    private CASMutator<Integer> mutator;

    public MemcachedSemaphoreSpyWay(MemcachedClient memcached, String name, int capacity) {
        super(capacity, memcached, name);

        mutator = new CASMutator<Integer>(memcached, new IntegerTranscoder());
        //Usually you don't want to put such a code in your constructor. That's a bad idea.
        //It's better to use @PostConstruct annotation and an appropriate IoC container.
        init(memcached, name);
    }


    @Override
    public boolean acquire() {
        try {
            mutator.cas(getName(), 0, INITIAL_EXP, incrementMutation);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        try {
            mutator.cas(getName(), 0, INITIAL_EXP, decrementMutation);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
