package com.maaz.wallet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maaz.wallet.dto.WalletCreditDto;
import com.maaz.wallet.dto.WalletUpdateBalanceReasonDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "/scripts/data.sql")
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testCreditWallet_Success() throws Exception {

        WalletCreditDto walletCreditDto = new WalletCreditDto();
        walletCreditDto.setWalletId(1L);
        walletCreditDto.setAmount(BigDecimal.valueOf(10D));
        WalletUpdateBalanceReasonDto walletUpdateBalanceReasonDto = new WalletUpdateBalanceReasonDto();
        walletUpdateBalanceReasonDto.setDescription("CARD PAYMENT");
        walletCreditDto.setWalletUpdateBalanceReason(walletUpdateBalanceReasonDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/credit")
                        .content(objectMapper.writeValueAsString(walletCreditDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("idempotency-key", "123"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.walletId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(110D)));

        mockMvc.perform(MockMvcRequestBuilders.post("/credit")
                        .content(objectMapper.writeValueAsString(walletCreditDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("idempotency-key", "234"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.walletId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(120D)));
    }

    @Test
    public void testDebitWallet_Success() throws Exception {

        WalletCreditDto walletCreditDto = new WalletCreditDto();
        walletCreditDto.setWalletId(2L);
        walletCreditDto.setAmount(BigDecimal.valueOf(10L));
        WalletUpdateBalanceReasonDto walletUpdateBalanceReasonDto = new WalletUpdateBalanceReasonDto();
        walletUpdateBalanceReasonDto.setDescription("CARD PAYMENT");
        walletCreditDto.setWalletUpdateBalanceReason(walletUpdateBalanceReasonDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/debit")
                        .content(objectMapper.writeValueAsString(walletCreditDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("idempotency-key", "123"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.walletId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(90D)));

        mockMvc.perform(MockMvcRequestBuilders.post("/debit")
                        .content(objectMapper.writeValueAsString(walletCreditDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("idempotency-key", "234"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.walletId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(80D)));
    }
}
