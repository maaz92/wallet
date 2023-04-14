package com.maaz.wallet.view;

import com.maaz.wallet.enums.WalletUpdateBalanceType;
import com.maaz.wallet.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransactionViewTest {


    @Test
    public void testFrom_Successful() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCreatedAt(ZonedDateTime.now());
        transaction.setAmount(BigDecimal.valueOf(1L));
        transaction.setWalletId(1L);
        transaction.setType(WalletUpdateBalanceType.CREDIT);
        transaction.setDescription("Card Payment");
        TransactionView transactionView = TransactionView.from(transaction);
        Assertions.assertEquals(transaction.getAmount(), transactionView.getAmount());
        Assertions.assertEquals(transaction.getId(), transactionView.getId());
        Assertions.assertEquals(transaction.getCreatedAt(), transactionView.getCreatedAt());
        Assertions.assertEquals(transaction.getWalletId(), transactionView.getWalletId());
        Assertions.assertEquals(transaction.getDescription(), transactionView.getDescription());
        Assertions.assertEquals(transaction.getDescription(), transactionView.getDescription());
    }
}
