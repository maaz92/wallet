package com.maaz.wallet.repository;

import com.maaz.wallet.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
    Page<Transaction> findAllByWalletId(Long id, Pageable pageable);
}
