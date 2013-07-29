package com.othelle.samples.memcached;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * author: v.vlasov
 */
public abstract class SemaphoreTest {

    public static final int CAPACITY = 10;
    public static final String SEMAPHORE_ID = "semaphoreSpyWay";

    private Semaphore semaphore;


    public abstract Semaphore getSemaphore(int capacity);

    @Before
    public void setup() throws IOException {
        semaphore = getSemaphore(CAPACITY);
    }


    @Test
    public void testAcquireReleaseSingleThread() {
        //Trying to release unlocked semaphore
        assertThat(semaphore.release(), Matchers.equalTo(false));

        for (int i = 0; i < CAPACITY; i++) {
            assertThat(semaphore.acquire(), Matchers.equalTo(true));
        }

        //Trying to release unlocked semaphore
        assertThat(semaphore.acquire(), Matchers.equalTo(false));


        //release then acquire
        assertThat(semaphore.release(), Matchers.equalTo(true));
        assertThat(semaphore.acquire(), Matchers.equalTo(true));
    }

    @Test
    public void randomizedProcessesTest() throws InterruptedException {
        //Running batch of threads with a random sleep period attempting to acquire semaphore and checking the semaphore bounds
        //That's not a REAL test. Just a demonstration that everything seems (but not guaranteed) to be working
        final int threadCount = 500;
        final CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        while (!semaphore.acquire()) {
                            try {
                                int current = semaphore.current();
                                if (current > CAPACITY || current < 0)
                                    System.exit(-200);


                                Thread.sleep((long) (1000 * Math.random()));
                            } catch (InterruptedException e) {
                                System.out.println("Interrupted");
                                return;
                            } finally {
                                semaphore.release();
                            }
                        }
                        System.out.println("Completed");
                    } finally {
                        latch.countDown();
                    }

                }
            }.start();
        }
        latch.await(100, TimeUnit.SECONDS);
    }
}
