package com.ajw.bond.model;

public class IdempotenceKey {

    private final String shard;

    private final String key;

    public IdempotenceKey(
            String shard,
            String key) {
        this.shard = shard;
        this.key = key;
    }

    public String getShard() {
        return shard;
    }

    public String getKey() {
        return key;
    }

}
