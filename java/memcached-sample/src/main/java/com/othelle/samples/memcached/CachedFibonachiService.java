package com.othelle.samples.memcached;

import com.google.code.ssm.api.ParameterValueKeyProvider;
import com.google.code.ssm.api.ReadThroughSingleCache;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Attempting to create a cached function using spring-memcached
 * author: v.vlasov
 */
@Component(value = "fibonachiService")
public class CachedFibonachiService {
    public static final String NAMESPACE = "CachedCalculations";
    public static final int EXPIRATION = 3600;

    @Autowired
    private MemcachedClient memcached;

    /**
     * I'm implementing fibonachi as a recursive function to check how fast it would be to lookup cache instead of recalculating
     *
     * @param number the number in fibonachi sequence.
     * @return the fibo number
     */
    @ReadThroughSingleCache(namespace = NAMESPACE, expiration = EXPIRATION)
    public long apply(@ParameterValueKeyProvider int number) {
        if (number < 0) throw new IllegalArgumentException("Unsupported number provided: " + number);
        return apply0(number);

    }

    /**
     * Unfortunately there is no way to cache recursive calls. So we are using our own caching
     *
     * @param number
     * @return
     */
    protected long apply0(int number) {
        String key = NAMESPACE + getClass().getName() + ".apply." + number;
        Object cachedValue = memcached.get(key);
        if (cachedValue != null) {
            return (Long) cachedValue;
        }

        switch (number) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                long l1 = apply(number - 2);
                long l2 = apply(number - 1);

                System.out.println("apply0(" + number + ") = " + l1 + "+" + l2);
                long result = l1 + l2;
                memcached.set(key, EXPIRATION, result);
                return result;
        }
    }
}
