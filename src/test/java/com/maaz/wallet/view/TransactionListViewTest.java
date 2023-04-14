package com.maaz.wallet.view;

import com.maaz.wallet.helper.TransactionObjects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionListViewTest {

    @Test
    public void testFrom_successful() {
        TransactionListView transactionListView = TransactionListView.from(TransactionObjects.getTransactionPage());
        Assertions.assertEquals(transactionListView.getNumberOfPages(), TransactionObjects.getTransactionPage().getTotalPages());
        Assertions.assertEquals(transactionListView.getNumberOfTransactionsInCurrentPage(), TransactionObjects.getTransactionPage().getNumberOfElements());
        Assertions.assertEquals(transactionListView.getTotalNumberOfTransactions(), TransactionObjects.getTransactionPage().getTotalElements());
        List<TransactionView> transactionViewList1 = TransactionObjects.getTransactionPage().getContent().stream().map(TransactionView::from).collect(Collectors.toList());
        Assertions.assertEquals(transactionListView.getTransactions().size(), transactionViewList1.size());
        Assertions.assertEquals(transactionListView.getTransactions().get(0).getId(), transactionViewList1.get(0).getId());
        Assertions.assertEquals(transactionListView.getTransactions().get(1).getId(), transactionViewList1.get(1).getId());
    }
}
