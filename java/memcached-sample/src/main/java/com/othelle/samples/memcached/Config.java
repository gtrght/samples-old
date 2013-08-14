package com.othelle.samples.memcached;

/**
 * author: v.vlasov
 */
public interface Config {
    //I'm using memcached installed on my linux virtualbox. You probably would need to replace guest-server with localhost
    //It's always better to use @Value("${memcached.server}") but that's ok for testing purposes.
    String CONNECTION_STRING = "localhost:11211";
}
