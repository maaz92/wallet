package com.maaz.wallet.view;

import com.maaz.wallet.model.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class WalletViewTest {

    @Test
    public void testFrom_successful() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(10L));
        wallet.setCreatedAt(ZonedDateTime.now());
        wallet.setCustomerId(1L);
        wallet.setUpdatedAt(ZonedDateTime.now());
        WalletView walletView = WalletView.from(wallet);
        Assertions.assertEquals(wallet.getId(), walletView.getWalletId());
        Assertions.assertEquals(wallet.getCreatedAt(), walletView.getCreatedAt());
        Assertions.assertEquals(wallet.getUpdatedAt(), walletView.getUpdatedAt());
        Assertions.assertEquals(wallet.getBalance(), walletView.getBalance());
        Assertions.assertEquals(wallet.getCustomerId(), walletView.getCustomerId());
    }
}
