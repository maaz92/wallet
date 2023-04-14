package com.maaz.wallet.view;

import com.maaz.wallet.model.Wallet;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class WalletView {

    private Long walletId;

    private Long customerId;

    private BigDecimal balance;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    public static WalletView from(Wallet wallet) {
        WalletView walletView = new WalletView();
        walletView.setWalletId(wallet.getId());
        walletView.setCustomerId(wallet.getCustomerId());
        walletView.setBalance(wallet.getBalance());
        walletView.setCreatedAt(wallet.getCreatedAt());
        walletView.setUpdatedAt(wallet.getUpdatedAt());
        return walletView;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime created_at) {
        this.createdAt = created_at;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
