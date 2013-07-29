package com.othelle.samples.memcached;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;

/**
 * author: v.vlasov
 */
public class MemcachedSemaphoreTestSpyWay extends SemaphoreTest {
    @Override
    public Semaphore getSemaphore(int capacity) {
        try {
            MemcachedClient client = new MemcachedClient(AddrUtil.getAddresses("localhost:11211"));
            client.delete(SEMAPHORE_ID);
            return new MemcachedSemaphoreSpyWay(client, SEMAPHORE_ID, CAPACITY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
