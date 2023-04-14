package com.maaz.wallet.view;

import com.maaz.wallet.model.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionListView {

    private List<TransactionView> transactions;

    private Long totalNumberOfTransactions;

    private int numberOfTransactionsInCurrentPage;

    private int numberOfPages;

    public static TransactionListView from(Page<Transaction> transactionPage) {
        TransactionListView transactionListView = new TransactionListView();
        transactionListView.setTotalNumberOfTransactions(transactionPage.getTotalElements());
        transactionListView.setNumberOfTransactionsInCurrentPage(transactionPage.getNumberOfElements());
        transactionListView.setNumberOfPages(transactionPage.getTotalPages());
        List<Transaction> transactions = transactionPage.getContent();
        List<TransactionView> transactionViews = transactions.stream().map(TransactionView::from).collect(Collectors.toList());
        transactionListView.setTransactions(transactionViews);
        return transactionListView;
    }

    public List<TransactionView> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionView> transactions) {
        this.transactions = transactions;
    }

    public Long getTotalNumberOfTransactions() {
        return totalNumberOfTransactions;
    }

    public void setTotalNumberOfTransactions(Long totalNumberOfTransactions) {
        this.totalNumberOfTransactions = totalNumberOfTransactions;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public int getNumberOfTransactionsInCurrentPage() {
        return numberOfTransactionsInCurrentPage;
    }

    public void setNumberOfTransactionsInCurrentPage(int numberOfTransactionsInCurrentPage) {
        this.numberOfTransactionsInCurrentPage = numberOfTransactionsInCurrentPage;
    }
}
