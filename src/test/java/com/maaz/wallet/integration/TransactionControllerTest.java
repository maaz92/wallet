package com.maaz.wallet.integration;

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

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "/scripts/data.sql")
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetTransactions_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/wallet/1/transactions").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.transactions", Matchers.hasSize(2))).andExpect(MockMvcResultMatchers.jsonPath("$.transactions[0].id", Matchers.is(1))).andExpect(MockMvcResultMatchers.jsonPath("$.transactions[1].id", Matchers.is(2))).andExpect(MockMvcResultMatchers.jsonPath("$.transactions[0].walletId", Matchers.is(1))).andExpect(MockMvcResultMatchers.jsonPath("$.transactions[1].walletId", Matchers.is(1))).andExpect(MockMvcResultMatchers.jsonPath("$.transactions[0].type", Matchers.is("CREDIT"))).andExpect(MockMvcResultMatchers.jsonPath("$.transactions[1].type", Matchers.is("DEBIT"))).andExpect(MockMvcResultMatchers.jsonPath("$.transactions[0].amount", Matchers.is(150D))).andExpect(MockMvcResultMatchers.jsonPath("$.transactions[1].amount", Matchers.is(50D))).andExpect(MockMvcResultMatchers.jsonPath("$.totalNumberOfTransactions", Matchers.is(2))).andExpect(MockMvcResultMatchers.jsonPath("$.numberOfTransactionsInCurrentPage", Matchers.is(2))).andExpect(MockMvcResultMatchers.jsonPath("$.numberOfPages", Matchers.is(1)));
    }
}
