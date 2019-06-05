package com.ajw.bond.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = IdempotenceKey.Builder.class)
public class IdempotenceKey {

    private final String shard;

    private final String key;

    private final Boolean claimed;

    public IdempotenceKey(
            String shard,
            String key,
            Boolean claimed) {
        this.shard = shard;
        this.key = key;
        this.claimed = claimed;
    }

    public String getShard() {
        return shard;
    }

    public String getKey() {
        return key;
    }

    public Boolean getClaimed() {
        return claimed;
    }

    @JsonPOJOBuilder
    static class Builder {
        String shard;
        String key;
        Boolean claimed;

        Builder withShard(String shard) {
            this.shard = shard;
            return this;
        }

        Builder withKey(String key) {
            this.key = key;
            return this;
        }


        Builder withClaimed(Boolean claimed) {
            this.claimed = claimed;
            return this;
        }

        public IdempotenceKey build() {
            return new IdempotenceKey(shard, key, claimed);
        }
    }


}
