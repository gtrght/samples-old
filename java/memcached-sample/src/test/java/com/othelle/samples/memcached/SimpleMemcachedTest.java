package com.othelle.samples.memcached;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.CASMutation;
import net.spy.memcached.CASMutator;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.IntegerTranscoder;
import net.spy.memcached.transcoders.LongTranscoder;
import net.spy.memcached.transcoders.Transcoder;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * author: v.vlasov
 */
public class SimpleMemcachedTest {

    private Transcoder<Integer> integerTranscoder;
    private Transcoder<Long> longTranscoder;
    private MemcachedClient client;

    @Before
    public void setup() throws IOException {
        client = new MemcachedClient(AddrUtil.getAddresses("localhost:11211"));
        integerTranscoder = new IntegerTranscoder();
        longTranscoder = new LongTranscoder();
    }

    @Test
    public void testSimpleOperationsOnInts() {
        String key = "key1";
        assertThat(client.set(key, 60, "1").getStatus().isSuccess(), Matchers.equalTo(true));

        //You are unable to add while data is not expired
        assertThat(client.add(key, 60, 2).getStatus().isSuccess(), Matchers.equalTo(false));

        //Testing incr/decr
        Object value = client.get(key);
        assertThat(client.incr(key, 1, 1), Matchers.equalTo(2l));
        assertThat((String) client.get(key), Matchers.equalTo("2"));


        //Trying to override a value by key
        assertThat(client.set(key, 60, "string").getStatus().isSuccess(), Matchers.equalTo(true));

        //then adding to key
        assertThat(client.replace(key, 60, "string").getStatus().isSuccess(), Matchers.equalTo(true));
    }

    @Test
    public void testIncDecrOfInteger() {
        String intKey = "intKey";
        assertThat(client.set(intKey, 60, 1).getStatus().isSuccess(), Matchers.equalTo(true));
        //The value should be a string \"1\" to be incremented, not an int!
        assertThat(client.incr(intKey, 2), Matchers.equalTo(-1l));

        //Same here
        assertThat(client.decr(intKey, 1), Matchers.equalTo(-1l));
    }

    @Test
    public void testIncDecrOfLong() {
        String longKey = "longKey";
        assertThat(client.set(longKey, 60, 0l).getStatus().isSuccess(), Matchers.equalTo(true));
        //The value should be a string \"10\" inc/decr, or 0
        assertThat(client.incr(longKey, 2), Matchers.not(Matchers.equalTo(2l)));

        //Same here
        assertThat(client.decr(longKey, 1), Matchers.not(Matchers.equalTo(1l)));
    }

    @Test
    public void testCasOperations() throws Exception {
        String casKey = "casKey";
        CASMutator<Integer> mutator = new CASMutator<Integer>(client, integerTranscoder);

        mutator.cas(casKey, 0, 0, new CASMutation<Integer>() {
            @Override
            public Integer getNewValue(Integer current) {
                return 2;
            }
        });

        assertThat((Integer) client.get(casKey), Matchers.equalTo(2));


        mutator.cas(casKey, 0, 0, new CASMutation<Integer>() {
            @Override
            public Integer getNewValue(Integer current) {
                if (current == 2)
                    return current;
                return 3;
            }
        });
    }

    @Test
    public void testPrependAppend() {
        String stringKey = "testPrependAppendString";
        assertThat(client.set(stringKey, 60, "0").getStatus().isSuccess(), Matchers.equalTo(true));
        assertThat(client.set(stringKey, 60, "1").getStatus().isSuccess(), Matchers.equalTo(true));

        assertThat(client.append(-1l, stringKey, "60").getStatus().isSuccess(), Matchers.equalTo(true));
        assertThat(client.get(stringKey), Matchers.equalTo((Object) "160"));

        assertThat(client.prepend(-1l, stringKey, "20").getStatus().isSuccess(), Matchers.equalTo(true));
        assertThat(client.get(stringKey), Matchers.equalTo((Object) "20160"));


        String longKey = "testPrependAppendLong";
        assertThat(client.set(longKey, 60, 1l, longTranscoder).getStatus().isSuccess(), Matchers.equalTo(true));

        assertThat(client.append(-1l, longKey, 5l, longTranscoder).getStatus().isSuccess(), Matchers.equalTo(true));
        //since values are aligned by byte: 5=0000 0101, 1=0000 0001 => 1+5 = 0001 0000 0101 = 261l
        assertThat(client.get(longKey, longTranscoder), Matchers.equalTo(261l));

        assertThat(client.prepend(-1l, longKey, 3l).getStatus().isSuccess(), Matchers.equalTo(true));
        //0000 0011(3) + 0000 0001 0000 0101 (261) = 196869l
        assertThat(client.get(longKey, longTranscoder), Matchers.equalTo(196869L));


        String intKey = "testPrependAppendInteger";
        assertThat(client.set(intKey, 60, 1, integerTranscoder).getStatus().isSuccess(), Matchers.equalTo(true));
        assertThat(client.append(-1l, intKey, 5, integerTranscoder).getStatus().isSuccess(), Matchers.equalTo(true));
        assertThat(client.get(intKey, integerTranscoder), Matchers.equalTo(261));
        assertThat(client.prepend(-1l, intKey, 3).getStatus().isSuccess(), Matchers.equalTo(true));
        //the same apply to integer. 0000 0011(3) + 0000 0001 0000 0101 (261) = 196869l
        assertThat(client.get(intKey, integerTranscoder), Matchers.equalTo(196869));
    }
}
