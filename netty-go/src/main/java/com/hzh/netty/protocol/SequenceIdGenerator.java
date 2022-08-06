package com.hzh.netty.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
public abstract class SequenceIdGenerator {
    private static final AtomicInteger id = new AtomicInteger();

    public static int nextId() {
        return id.incrementAndGet();
    }
}
