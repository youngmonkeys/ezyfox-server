package com.tvd12.ezyfoxserver.service.impl;

import com.tvd12.ezyfox.sercurity.EzySHA256;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGenerator;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EzySimpleSessionTokenGenerator implements EzySessionTokenGenerator {

    private final AtomicLong counter;
    private final String serverNodeName;

    public EzySimpleSessionTokenGenerator() {
        this("ezyfox");
    }

    public EzySimpleSessionTokenGenerator(String serverNodeName) {
        this.counter = new AtomicLong();
        this.serverNodeName = serverNodeName;
    }

    @Override
    public String generate() {
        String token = new StringBuilder()
            .append(serverNodeName).append("#")
            .append(getCount()).append("#")
            .append(UUID.randomUUID()).append("#")
            .append(System.currentTimeMillis())
            .toString();
        return EzySHA256.cryptUtfToLowercase(token);
    }

    private long getCount() {
        return counter.incrementAndGet();
    }

}
