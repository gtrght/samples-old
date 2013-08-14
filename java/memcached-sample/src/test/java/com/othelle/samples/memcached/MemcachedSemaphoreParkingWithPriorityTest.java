package com.othelle.samples.memcached;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * author: v.vlasov
 */
public class MemcachedSemaphoreParkingWithPriorityTest extends SemaphoreTest {
    @Override
    public Semaphore getSemaphore(int capacity) {
        try {
            MemcachedClient client = new MemcachedClient(AddrUtil.getAddresses(Config.CONNECTION_STRING));
            client.delete(SEMAPHORE_ID);
            return new MemcachedSemaphoreParkingPrioritySemaphore(client, SEMAPHORE_ID, CAPACITY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test(timeout = 1000)
    public void testAcquireReleaseSingleThread() {
        //Trying to release unlocked semaphore
        assertThat(semaphore.release(), Matchers.equalTo(false));

        for (int i = 0; i < CAPACITY; i++) {
            assertThat(semaphore.acquire(), Matchers.equalTo(true));
        }

        final CountDownLatch latch = new CountDownLatch(2);

        //Trying to release unlocked semaphore, the thread is getting locked until succeed
        new Thread() {
            @Override
            public void run() {
                assertThat(semaphore.acquire(), Matchers.equalTo(true));
                latch.countDown();
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                assertThat(semaphore.release(), Matchers.equalTo(true));
                latch.countDown();
            }
        }.start();

        //release then acquire
        assertThat(semaphore.release(), Matchers.equalTo(true));
        assertThat(semaphore.acquire(), Matchers.equalTo(true));
    }
}
