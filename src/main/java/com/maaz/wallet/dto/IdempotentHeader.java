package com.maaz.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdempotentHeader {

    @JsonProperty("Idempotency-Key")
    private String idempotencyKey;

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }
}
