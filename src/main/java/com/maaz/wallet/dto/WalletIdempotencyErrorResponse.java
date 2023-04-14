package com.maaz.wallet.dto;

public class WalletIdempotencyErrorResponse {

    String message;

    public WalletIdempotencyErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
