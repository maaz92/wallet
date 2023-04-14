package com.maaz.wallet.controller;

import com.maaz.wallet.dto.WalletCreditDto;
import com.maaz.wallet.dto.WalletDebitDto;
import com.maaz.wallet.exception.WalletValidationException;
import com.maaz.wallet.repository.IdempotencyTrackerRepository;
import com.maaz.wallet.service.TransactionService;
import com.maaz.wallet.service.WalletService;
import com.maaz.wallet.view.WalletView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Validated
@RestController
public class WalletController {

    private static final String IDEMPOTENCY_KEY = "idempotency-key";
    private static final String IDEMPOTENCY_KEY_VALIDATION_TEXT = "The header Idempotency-Key must not be null";
    @Autowired
    private WalletService walletService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private IdempotencyTrackerRepository idempotencyResponseRepository;

    @PostMapping(value = "/credit", consumes = "application/json", produces = "application/json")
    public WalletView updateWalletBalance(@Valid @RequestBody WalletCreditDto walletCreditDto, @RequestHeader Map<String, String> headers) {
        String idempotencyKey = headers.get(IDEMPOTENCY_KEY);
        if (idempotencyKey == null) {
            throw new WalletValidationException(IDEMPOTENCY_KEY_VALIDATION_TEXT);
        }
        return this.walletService.creditWalletBalance(idempotencyKey, walletCreditDto);
    }

    @PostMapping(value = "/debit", consumes = "application/json", produces = "application/json")
    public WalletView updateWalletBalance(@Valid @RequestBody WalletDebitDto walletDebitDto, @RequestHeader Map<String, String> headers) {
        String idempotencyKey = headers.get(IDEMPOTENCY_KEY);
        if (idempotencyKey == null) {
            throw new WalletValidationException(IDEMPOTENCY_KEY_VALIDATION_TEXT);
        }
        return this.walletService.debitWalletBalance(idempotencyKey, walletDebitDto);
    }
}
