package com.maaz.wallet.view;

import com.maaz.wallet.enums.WalletUpdateBalanceType;
import com.maaz.wallet.model.Transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransactionView {

    private Long id;

    private Long walletId;

    private WalletUpdateBalanceType type;

    private String description;

    private BigDecimal amount;

    private ZonedDateTime createdAt;

    public static TransactionView from(Transaction transaction) {
        TransactionView transactionView = new TransactionView();
        transactionView.setAmount(transaction.getAmount());
        transactionView.setType(transaction.getType());
        transactionView.setDescription(transaction.getDescription());
        transactionView.setId(transaction.getId());
        transactionView.setWalletId(transaction.getWalletId());
        transactionView.setCreatedAt(transaction.getCreatedAt());
        return transactionView;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WalletUpdateBalanceType getType() {
        return type;
    }

    public void setType(WalletUpdateBalanceType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
