package com.maaz.wallet.service;

import com.maaz.wallet.dto.WalletCreditDto;
import com.maaz.wallet.dto.WalletDebitDto;
import com.maaz.wallet.enums.WalletUpdateBalanceType;
import com.maaz.wallet.exception.WalletIdempotencyException;
import com.maaz.wallet.exception.WalletValidationException;
import com.maaz.wallet.model.IdempotencyTracker;
import com.maaz.wallet.model.Transaction;
import com.maaz.wallet.model.Wallet;
import com.maaz.wallet.repository.IdempotencyTrackerRepository;
import com.maaz.wallet.repository.TransactionRepository;
import com.maaz.wallet.repository.WalletRepository;
import com.maaz.wallet.view.WalletView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class WalletService {

    public static final String WALLET_NOT_FOUND = "Wallet Not Found";
    public static final String CANNOT_DEBIT_TO_NEGATIVE = "Cannot debit wallet with an amount greater than the balance";
    public static final String CREDIT_REQUEST_KEY = "POST: /credit";
    public static final String DEBIT_REQUEST_KEY = "POST: /debit";
    public static final Long IDEMPOTENCY_TTL = 3L;
    public static final String IDEMPOTENCY_ERROR_MESSAGE = "Duplicate request. Please use a different idempotency key or try after 3 seconds";
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private IdempotencyTrackerRepository idempotencyResponseRepository;

    public String getUniqueKeyForCreditRequest(String idempotencyKey) {
        return CREDIT_REQUEST_KEY+idempotencyKey;
    }

    public String getUniqueKeyForDebitRequest(String idempotencyKey) {
        return DEBIT_REQUEST_KEY+idempotencyKey;
    }

    @Transactional
    public WalletView creditWalletBalance(String idempotencyKey, WalletCreditDto walletCreditDto) {
        String uniqueKey = getUniqueKeyForCreditRequest(idempotencyKey);
        ZonedDateTime now = ZonedDateTime.now();
        Optional<IdempotencyTracker> optionalIdempotencyResponse = idempotencyResponseRepository.findById(uniqueKey);
        if (optionalIdempotencyResponse.isPresent()) {

            if (optionalIdempotencyResponse.get().getCreatedAt().plusSeconds(IDEMPOTENCY_TTL).isAfter(now)) {
                throw new WalletIdempotencyException(IDEMPOTENCY_ERROR_MESSAGE);
            }
            idempotencyResponseRepository.delete(optionalIdempotencyResponse.get());
        }
        Long walletId = walletCreditDto.getWalletId();
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (!optionalWallet.isPresent()) {
            throw new WalletValidationException(WALLET_NOT_FOUND);
        }
        Wallet updatedWallet = optionalWallet.get();
        updatedWallet.setBalance(updatedWallet.getBalance().add(walletCreditDto.getAmount()));
        updatedWallet.setUpdatedAt(now);
        updatedWallet = walletRepository.save(updatedWallet);
        Transaction transaction = new Transaction();
        transaction.setType(WalletUpdateBalanceType.CREDIT);
        transaction.setWalletId(updatedWallet.getId());
        transaction.setAmount(walletCreditDto.getAmount());
        transaction.setDescription(walletCreditDto.getWalletUpdateBalanceReason().getDescription());
        transaction.setCreatedAt(now);
        transactionRepository.save(transaction);
        IdempotencyTracker idempotencyResponse = new IdempotencyTracker();
        idempotencyResponse.setUniqueKey(uniqueKey);
        idempotencyResponse.setCreatedAt(now);
        idempotencyResponseRepository.save(idempotencyResponse);
        return WalletView.from(updatedWallet);
    }

    @Transactional
    public WalletView debitWalletBalance(String idempotencyKey, WalletDebitDto walletDebitDto) {
        String uniqueKey = getUniqueKeyForDebitRequest(idempotencyKey);
        ZonedDateTime now = ZonedDateTime.now();
        Optional<IdempotencyTracker> optionalIdempotencyResponse = idempotencyResponseRepository.findById(uniqueKey);
        if (optionalIdempotencyResponse.isPresent()) {

            if (optionalIdempotencyResponse.get().getCreatedAt().plusSeconds(IDEMPOTENCY_TTL).isAfter(now)) {
                throw new WalletIdempotencyException(IDEMPOTENCY_ERROR_MESSAGE);
            }
            idempotencyResponseRepository.delete(optionalIdempotencyResponse.get());
        }
        Long walletId = walletDebitDto.getWalletId();
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (!optionalWallet.isPresent()) {
            throw new WalletValidationException(WALLET_NOT_FOUND);
        }
        if (walletDebitDto.getAmount().compareTo(optionalWallet.get().getBalance()) > 0) {
            throw new WalletValidationException(CANNOT_DEBIT_TO_NEGATIVE);
        }
        Wallet updatedWallet = optionalWallet.get();
        updatedWallet.setBalance(optionalWallet.get().getBalance().subtract(walletDebitDto.getAmount()));
        updatedWallet.setUpdatedAt(now);
        updatedWallet = walletRepository.save(updatedWallet);
        Transaction transaction = new Transaction();
        transaction.setType(WalletUpdateBalanceType.DEBIT);
        transaction.setWalletId(updatedWallet.getId());
        transaction.setAmount(walletDebitDto.getAmount());
        transaction.setDescription(walletDebitDto.getWalletUpdateBalanceReason().getDescription());
        transaction.setCreatedAt(now);
        transactionRepository.save(transaction);
        IdempotencyTracker idempotencyResponse = new IdempotencyTracker();
        idempotencyResponse.setUniqueKey(uniqueKey);
        idempotencyResponse.setCreatedAt(now);
        idempotencyResponseRepository.save(idempotencyResponse);
        return WalletView.from(updatedWallet);
    }
}
