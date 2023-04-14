package com.maaz.wallet.exception;

public class WalletIdempotencyException extends RuntimeException {

    public WalletIdempotencyException(String message) {
        super(message);
    }
}
