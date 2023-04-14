package com.maaz.wallet.dto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;

public class WalletCreditDto {

    @NotNull
    @Valid
    private WalletUpdateBalanceReasonDto walletUpdateBalanceReason;

    @NotNull
    private Long walletId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "10.0", message = "The minimum amount you can send is £10")
    @DecimalMax(value = "10000.0", message = "The maximum amount you can send is £10,000")
    @Digits(integer = 10, fraction = 2, message = "Amount cannot contain more than 2 decimal places or more than 10 integer places")
    private BigDecimal amount;

    public WalletUpdateBalanceReasonDto getWalletUpdateBalanceReason() {
        return walletUpdateBalanceReason;
    }

    public void setWalletUpdateBalanceReason(WalletUpdateBalanceReasonDto walletUpdateBalanceReason) {
        this.walletUpdateBalanceReason = walletUpdateBalanceReason;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
