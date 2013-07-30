package com.othelle.samples.memcached;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;

/**
 * author: v.vlasov
 */
public class MemcachedSemaphoreBasicTest extends SemaphoreTest {

    //I'm using memcached installed on my linux virtualbox. You probably would need to replace 169.254.192.223 with localhost
    public static final String CONNECTION_STRING = "169.254.192.223:11211";

    @Override
    public Semaphore getSemaphore(int capacity) {
        try {
            MemcachedClient client = new MemcachedClient(AddrUtil.getAddresses(CONNECTION_STRING));
            client.delete(SEMAPHORE_ID);
            return new MemcachedSemaphoreBasic(client, SEMAPHORE_ID, CAPACITY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
