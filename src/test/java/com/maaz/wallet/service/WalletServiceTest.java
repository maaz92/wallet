package com.maaz.wallet.service;

import com.maaz.wallet.dto.WalletCreditDto;
import com.maaz.wallet.dto.WalletDebitDto;
import com.maaz.wallet.dto.WalletUpdateBalanceReasonDto;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private IdempotencyTrackerRepository idempotencyResponseRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void creditWalletBalance_HappyCase() {
        String idempotencyKey = "123";
        ZonedDateTime currentTime = ZonedDateTime.now();
        WalletCreditDto walletCreditDto = new WalletCreditDto();
        walletCreditDto.setWalletId(1L);
        walletCreditDto.setAmount(BigDecimal.valueOf(10L));
        WalletUpdateBalanceReasonDto walletUpdateBalanceReasonDto = new WalletUpdateBalanceReasonDto();
        walletUpdateBalanceReasonDto.setDescription("CARD PAYMENT");
        walletCreditDto.setWalletUpdateBalanceReason(walletUpdateBalanceReasonDto);
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(100L));
        wallet.setCustomerId(1L);
        wallet.setCreatedAt(currentTime);
        wallet.setUpdatedAt(currentTime);
        Mockito.when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);
        WalletView walletView = walletService.creditWalletBalance(idempotencyKey, walletCreditDto);
        Assertions.assertEquals(walletView.getBalance(), BigDecimal.valueOf(110L));
        Assertions.assertEquals(walletView.getWalletId(), 1L);
        Assertions.assertEquals(walletView.getCustomerId(), 1L);
        Assertions.assertEquals(walletView.getCreatedAt(), currentTime);
        Assertions.assertEquals(walletView.getUpdatedAt(), wallet.getUpdatedAt());
        Mockito.verify(walletRepository, Mockito.times(1)).save(wallet);
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        Mockito.verify(transactionRepository, Mockito.times(1)).save(transactionArgumentCaptor.capture());
        Transaction capturedTransaction = transactionArgumentCaptor.getValue();
        Assertions.assertEquals(capturedTransaction.getWalletId(), walletCreditDto.getWalletId());
        Assertions.assertEquals(capturedTransaction.getDescription(), walletCreditDto.getWalletUpdateBalanceReason().getDescription());
        Assertions.assertEquals(capturedTransaction.getAmount(), walletCreditDto.getAmount());
        Assertions.assertEquals(capturedTransaction.getType(), WalletUpdateBalanceType.CREDIT);
        Assertions.assertNull(capturedTransaction.getId());
        Assertions.assertEquals(capturedTransaction.getCreatedAt(), walletView.getUpdatedAt());
        ArgumentCaptor<IdempotencyTracker> idempotencyArgumentCaptor = ArgumentCaptor.forClass(IdempotencyTracker.class);
        Mockito.verify(idempotencyResponseRepository, Mockito.times(1)).save(idempotencyArgumentCaptor.capture());
        IdempotencyTracker capturedIdempotencyResponse = idempotencyArgumentCaptor.getValue();
        Assertions.assertEquals(capturedIdempotencyResponse.getUniqueKey(), walletService.getUniqueKeyForCreditRequest(idempotencyKey));
        Assertions.assertEquals(capturedIdempotencyResponse.getCreatedAt(), walletView.getUpdatedAt());
    }

    @Test
    public void creditWalletBalance_IdempotentRequest() {
        String idempotencyKey = "123";
        ZonedDateTime currentTime = ZonedDateTime.now();
        WalletCreditDto walletCreditDto = new WalletCreditDto();
        walletCreditDto.setWalletId(1L);
        walletCreditDto.setAmount(BigDecimal.valueOf(10L));
        WalletUpdateBalanceReasonDto walletUpdateBalanceReasonDto = new WalletUpdateBalanceReasonDto();
        walletUpdateBalanceReasonDto.setDescription("CARD PAYMENT");
        walletCreditDto.setWalletUpdateBalanceReason(walletUpdateBalanceReasonDto);
        IdempotencyTracker idempotencyResponse = new IdempotencyTracker();
        idempotencyResponse.setUniqueKey(walletService.getUniqueKeyForCreditRequest(idempotencyKey));
        idempotencyResponse.setCreatedAt(currentTime.minusSeconds(1));
        Mockito.when(idempotencyResponseRepository.findById(walletService.getUniqueKeyForCreditRequest(idempotencyKey))).thenReturn(Optional.of(idempotencyResponse));
        WalletIdempotencyException walletIdempotencyException = Assertions.assertThrows(WalletIdempotencyException.class, () -> walletService.creditWalletBalance(idempotencyKey, walletCreditDto));
        Assertions.assertEquals(walletIdempotencyException.getMessage(), WalletService.IDEMPOTENCY_ERROR_MESSAGE);
    }

    @Test
    public void creditWalletBalance_WalletNotFound() {
        String idempotencyKey = "123";
        ZonedDateTime currentTime = ZonedDateTime.now();
        WalletCreditDto walletCreditDto = new WalletCreditDto();
        walletCreditDto.setWalletId(1L);
        walletCreditDto.setAmount(BigDecimal.valueOf(10L));
        WalletUpdateBalanceReasonDto walletUpdateBalanceReasonDto = new WalletUpdateBalanceReasonDto();
        walletUpdateBalanceReasonDto.setDescription("CARD PAYMENT");
        walletCreditDto.setWalletUpdateBalanceReason(walletUpdateBalanceReasonDto);
        Mockito.when(walletRepository.findById(walletCreditDto.getWalletId())).thenReturn(Optional.empty());
        WalletValidationException walletValidationException = Assertions.assertThrows(WalletValidationException.class, () -> walletService.creditWalletBalance(idempotencyKey, walletCreditDto));
        Assertions.assertEquals(walletValidationException.getMessage(), WalletService.WALLET_NOT_FOUND);
    }

    @Test
    public void debitWalletBalance_HappyCase() {
        String idempotencyKey = "123";
        ZonedDateTime currentTime = ZonedDateTime.now();
        WalletDebitDto walletDebitDto = new WalletDebitDto();
        walletDebitDto.setWalletId(1L);
        walletDebitDto.setAmount(BigDecimal.valueOf(10L));
        WalletUpdateBalanceReasonDto walletUpdateBalanceReasonDto = new WalletUpdateBalanceReasonDto();
        walletUpdateBalanceReasonDto.setDescription("CARD PAYMENT");
        walletDebitDto.setWalletUpdateBalanceReason(walletUpdateBalanceReasonDto);
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(100L));
        wallet.setCustomerId(1L);
        wallet.setCreatedAt(currentTime);
        wallet.setUpdatedAt(currentTime);
        Mockito.when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);
        WalletView walletView = walletService.debitWalletBalance(idempotencyKey, walletDebitDto);
        Assertions.assertEquals(walletView.getBalance(), BigDecimal.valueOf(90L));
        Assertions.assertEquals(walletView.getWalletId(), 1L);
        Assertions.assertEquals(walletView.getCustomerId(), 1L);
        Assertions.assertEquals(walletView.getCreatedAt(), currentTime);
        Assertions.assertEquals(walletView.getUpdatedAt(), wallet.getUpdatedAt());
        Mockito.verify(walletRepository, Mockito.times(1)).save(wallet);
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        Mockito.verify(transactionRepository, Mockito.times(1)).save(transactionArgumentCaptor.capture());
        Transaction capturedTransaction = transactionArgumentCaptor.getValue();
        Assertions.assertEquals(capturedTransaction.getWalletId(), walletDebitDto.getWalletId());
        Assertions.assertEquals(capturedTransaction.getDescription(), walletDebitDto.getWalletUpdateBalanceReason().getDescription());
        Assertions.assertEquals(capturedTransaction.getAmount(), walletDebitDto.getAmount());
        Assertions.assertEquals(capturedTransaction.getType(), WalletUpdateBalanceType.DEBIT);
        Assertions.assertNull(capturedTransaction.getId());
        Assertions.assertEquals(capturedTransaction.getCreatedAt(), walletView.getUpdatedAt());
        ArgumentCaptor<IdempotencyTracker> idempotencyArgumentCaptor = ArgumentCaptor.forClass(IdempotencyTracker.class);
        Mockito.verify(idempotencyResponseRepository, Mockito.times(1)).save(idempotencyArgumentCaptor.capture());
        IdempotencyTracker capturedIdempotencyResponse = idempotencyArgumentCaptor.getValue();
        Assertions.assertEquals(capturedIdempotencyResponse.getUniqueKey(), walletService.getUniqueKeyForDebitRequest(idempotencyKey));
        Assertions.assertEquals(capturedIdempotencyResponse.getCreatedAt(), walletView.getUpdatedAt());
    }

    @Test
    public void debitWalletBalance_CannotDebitToNegative() {
        String idempotencyKey = "123";
        ZonedDateTime currentTime = ZonedDateTime.now();
        WalletDebitDto walletDebitDto = new WalletDebitDto();
        walletDebitDto.setWalletId(1L);
        walletDebitDto.setAmount(BigDecimal.valueOf(10L));
        WalletUpdateBalanceReasonDto walletUpdateBalanceReasonDto = new WalletUpdateBalanceReasonDto();
        walletUpdateBalanceReasonDto.setDescription("CARD PAYMENT");
        walletDebitDto.setWalletUpdateBalanceReason(walletUpdateBalanceReasonDto);
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(9L));
        wallet.setCustomerId(1L);
        wallet.setCreatedAt(currentTime);
        wallet.setUpdatedAt(currentTime);
        Mockito.when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);
        WalletValidationException walletIdempotencyException = Assertions.assertThrows(WalletValidationException.class, () -> walletService.debitWalletBalance(idempotencyKey, walletDebitDto));
        Assertions.assertEquals(walletIdempotencyException.getMessage(), WalletService.CANNOT_DEBIT_TO_NEGATIVE);
    }

    @Test
    public void debitWalletBalance_IdempotentRequest() {
        String idempotencyKey = "123";
        ZonedDateTime currentTime = ZonedDateTime.now();
        WalletDebitDto walletDebitDto = new WalletDebitDto();
        walletDebitDto.setWalletId(1L);
        walletDebitDto.setAmount(BigDecimal.valueOf(10L));
        WalletUpdateBalanceReasonDto walletUpdateBalanceReasonDto = new WalletUpdateBalanceReasonDto();
        walletUpdateBalanceReasonDto.setDescription("CARD PAYMENT");
        walletDebitDto.setWalletUpdateBalanceReason(walletUpdateBalanceReasonDto);
        IdempotencyTracker idempotencyResponse = new IdempotencyTracker();
        idempotencyResponse.setUniqueKey(walletService.getUniqueKeyForDebitRequest(idempotencyKey));
        idempotencyResponse.setCreatedAt(currentTime.minusSeconds(1));
        Mockito.when(idempotencyResponseRepository.findById(walletService.getUniqueKeyForDebitRequest(idempotencyKey))).thenReturn(Optional.of(idempotencyResponse));
        WalletIdempotencyException walletIdempotencyException = Assertions.assertThrows(WalletIdempotencyException.class, () -> walletService.debitWalletBalance(idempotencyKey, walletDebitDto));
        Assertions.assertEquals(walletIdempotencyException.getMessage(), WalletService.IDEMPOTENCY_ERROR_MESSAGE);
    }

    @Test
    public void debitWalletBalance_WalletNotFound() {
        String idempotencyKey = "123";
        ZonedDateTime currentTime = ZonedDateTime.now();
        WalletDebitDto walletDebitDto = new WalletDebitDto();
        walletDebitDto.setWalletId(1L);
        walletDebitDto.setAmount(BigDecimal.valueOf(10L));
        WalletUpdateBalanceReasonDto walletUpdateBalanceReasonDto = new WalletUpdateBalanceReasonDto();
        walletUpdateBalanceReasonDto.setDescription("CARD PAYMENT");
        walletDebitDto.setWalletUpdateBalanceReason(walletUpdateBalanceReasonDto);
        Mockito.when(walletRepository.findById(walletDebitDto.getWalletId())).thenReturn(Optional.empty());
        WalletValidationException walletValidationException = Assertions.assertThrows(WalletValidationException.class, () -> walletService.debitWalletBalance(idempotencyKey, walletDebitDto));
        Assertions.assertEquals(walletValidationException.getMessage(), WalletService.WALLET_NOT_FOUND);
    }
}
