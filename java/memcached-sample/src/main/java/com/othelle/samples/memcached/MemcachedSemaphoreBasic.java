package com.othelle.samples.memcached;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

/**
 * author: v.vlasov
 */
public class MemcachedSemaphoreBasic extends MemcachedSemaphoreBase implements Semaphore {
    public MemcachedSemaphoreBasic(MemcachedClient memcached, String name, int maxCapacity) {
        super(maxCapacity, memcached, name);
        //Usually you don't want to put such a code in your constructor. That's a bad idea.
        //It's better to use @PostConstruct annotation and an appropriate IoC container.
        init(memcached, name);
    }

    @Override
    public boolean acquire() {
        for (int i = 0; i < getRetries(); i++) {
            CASValue<Integer> value = memcached.gets(getName(), TRANSCODER);

            if (value.getValue() >= getCapacity())
                return false;

            if (memcached.cas(getName(), value.getCas(), value.getValue() + 1, TRANSCODER) == CASResponse.OK)
                return true;
        }

        return false;
    }

    @Override
    public boolean release() {
        for (int i = 0; i < getRetries(); i++) {
            CASValue<Integer> value = memcached.gets(getName(), TRANSCODER);

            if (value.getValue() <= 0)
                return false;

            if (memcached.cas(getName(), value.getCas(), value.getValue() - 1, TRANSCODER) == CASResponse.OK)
                return true;
        }

        return false;
    }

}
