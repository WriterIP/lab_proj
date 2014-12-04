package com.example;


import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class MccFactory {
    public static final Logger logger = Logger.getLogger(MccFactory.class.getName());
    private MemcachedClient mcc;

    static private MccFactory aMccFactory;

    private MccFactory(String server) throws IOException {
      mcc=new MemcachedClient(new InetSocketAddress(server, 11211));
    }

    public static synchronized MccFactory getConst(String server) throws IOException {
        if (aMccFactory == null) {
            aMccFactory = new MccFactory(server);
        }
        return aMccFactory;
    }


    public MemcachedClient getMemcachedClient() {
        return mcc;
    }


}