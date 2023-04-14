package com.maaz.wallet.controller;

import com.maaz.wallet.service.TransactionService;
import com.maaz.wallet.view.TransactionListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Validated
    @GetMapping(value = "wallet/{id}/transactions")
    public TransactionListView getWalletTransactions(@Valid @PathVariable @NotNull Long id, @Valid @RequestParam(defaultValue = "0") @Min(0) Integer page, @Valid @RequestParam(defaultValue = "10") @Min(1) @Max(10) Integer pageSize) {
        return transactionService.getTransactions(id, page, pageSize);
    }
}
