package com.othelle.samples.memcached;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

/**
 * author: v.vlasov
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:simple-spring-cache-beans.xml"})
public class CachedFibonachiServiceTest {

    @Autowired
    private CachedFibonachiService fibo;

    @Test
    public void testCalculations() {
        long[] expected = new long[]{0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368,
                75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169};

        for (int i = 0; i < expected.length; i++)
            assertThat(fibo.apply(i), Matchers.equalTo(expected[i]));
    }
}
