package com.maaz.wallet.service;

import com.maaz.wallet.model.Transaction;
import com.maaz.wallet.repository.TransactionRepository;
import com.maaz.wallet.view.TransactionListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionListView getTransactions(Long walletId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Transaction> transactionPage = transactionRepository.findAllByWalletId(walletId, pageable);
        return TransactionListView.from(transactionPage);
    }
}
