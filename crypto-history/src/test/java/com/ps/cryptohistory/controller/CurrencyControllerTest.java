package com.ps.cryptohistory.controller;

import com.ps.cryptohistory.service.cache.CurrencyCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CurrencyControllerTest {

    @Mock
    private CurrencyCacheService currencyCacheService;

    @InjectMocks
    private CurrencyController currencyController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    }

    @Test
    void getAllCurrencies_ShouldReturnListOfCurrencies() throws Exception {
        List<String> mockCurrencyList = Arrays.asList("USD", "EUR", "GBP");
        when(currencyCacheService.getCurrencyList()).thenReturn(mockCurrencyList);

        mockMvc.perform(get("/currency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0]").value("USD"))
                .andExpect(jsonPath("$[1]").value("EUR"))
                .andExpect(jsonPath("$[2]").value("GBP"));
    }
}
