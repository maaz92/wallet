package com.maaz.wallet.service;

import com.maaz.wallet.helper.TransactionObjects;
import com.maaz.wallet.model.Transaction;
import com.maaz.wallet.repository.TransactionRepository;
import com.maaz.wallet.view.TransactionListView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

@SpringBootTest
public class TransactionServiceTest {

    @InjectMocks
    TransactionService transactionService;

    @Mock
    TransactionRepository transactionRepository;

    @Test
    public void testGetTransactions_Empty() {
        Page<Transaction> emptyTransactionPage = TransactionObjects.getEmptyTransactionPage();
        Mockito.when(transactionRepository.findAllByWalletId(1L, PageRequest.of(0, 10))).thenReturn(emptyTransactionPage);
        TransactionListView transactionListView = transactionService.getTransactions(1L, 0, 10);
        Assertions.assertEquals(transactionListView.getTransactions(), Collections.emptyList());
        Assertions.assertEquals(transactionListView.getTotalNumberOfTransactions(), emptyTransactionPage.getTotalElements());
        Assertions.assertEquals(transactionListView.getNumberOfTransactionsInCurrentPage(), emptyTransactionPage.getNumberOfElements());
        Assertions.assertEquals(transactionListView.getNumberOfPages(), emptyTransactionPage.getTotalPages());
    }

    @Test
    public void testGetTransactions_NonEmpty() {
        Page<Transaction> transactionPage = TransactionObjects.getTransactionPage();
        Mockito.when(transactionRepository.findAllByWalletId(Mockito.any(), Mockito.any())).thenReturn(transactionPage);
        TransactionListView transactionListView = transactionService.getTransactions(1L, 0, 10);
        Assertions.assertEquals(transactionListView.getTransactions().size(), transactionPage.getContent().size());
        Assertions.assertEquals(transactionListView.getTransactions().get(0).getId(), transactionPage.getContent().get(0).getId());
        Assertions.assertEquals(transactionListView.getTransactions().get(1).getId(), transactionPage.getContent().get(1).getId());
        Assertions.assertEquals(transactionListView.getTotalNumberOfTransactions(), transactionPage.getTotalElements());
        Assertions.assertEquals(transactionListView.getNumberOfTransactionsInCurrentPage(), transactionPage.getNumberOfElements());
        Assertions.assertEquals(transactionListView.getNumberOfPages(), transactionPage.getTotalPages());
    }
}
