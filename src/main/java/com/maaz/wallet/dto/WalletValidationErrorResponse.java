package com.maaz.wallet.dto;

public class WalletValidationErrorResponse {

    String message;

    public WalletValidationErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
