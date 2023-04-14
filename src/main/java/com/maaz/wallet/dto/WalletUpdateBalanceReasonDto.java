package com.maaz.wallet.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class WalletUpdateBalanceReasonDto {

    @NotNull
    @NotBlank(message = "Description cannot be empty")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
