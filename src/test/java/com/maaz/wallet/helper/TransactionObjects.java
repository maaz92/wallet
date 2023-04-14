package com.maaz.wallet.helper;

import com.maaz.wallet.enums.WalletUpdateBalanceType;
import com.maaz.wallet.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionObjects {

    private static final Transaction transaction1;

    private static final Transaction transaction2;

    private static final Page<Transaction> transactionPage;

    private static final Page<Transaction> emptyTransactionPage;

    private static final List<Transaction> transactions;

    static {
        transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setCreatedAt(ZonedDateTime.now());
        transaction1.setAmount(BigDecimal.valueOf(2L));
        transaction1.setWalletId(1L);
        transaction1.setType(WalletUpdateBalanceType.CREDIT);
        transaction1.setDescription("Card Payment");

        transaction2 = new Transaction();
        transaction1.setId(3L);
        transaction1.setCreatedAt(ZonedDateTime.now());
        transaction1.setAmount(BigDecimal.valueOf(2L));
        transaction1.setWalletId(1L);
        transaction1.setType(WalletUpdateBalanceType.DEBIT);
        transaction1.setDescription("Cash Payment");

        transactions = Arrays.asList(transaction1, transaction2);

        transactionPage = new PageImpl(transactions, PageRequest.of(0, 10), 2);

        emptyTransactionPage = new PageImpl(new ArrayList(), PageRequest.of(0, 10), 0);

    }

    public static Transaction getTransaction1() {
        return transaction1;
    }

    public static Transaction getTransaction2() {
        return transaction2;
    }

    public static Page<Transaction> getTransactionPage() {
        return transactionPage;
    }

    public static List<Transaction> getTransactions() {
        return transactions;
    }

    public static Page<Transaction> getEmptyTransactionPage() {
        return emptyTransactionPage;
    }
}
