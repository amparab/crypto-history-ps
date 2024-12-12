package com.publicissapient.cryptohistory.controller;

import com.publicissapient.cryptohistory.model.HistoryResponse;
import com.publicissapient.cryptohistory.template.BitcoinHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.publicissapient.cryptohistory.utils.Util.SUCCESS;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class BitcoinHistoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BitcoinHistoryService bitcoinHistoryService;

    @InjectMocks
    private BitcoinHistoryController bitcoinHistoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bitcoinHistoryController).build();
    }

    @Test
    public void testGetBitcoinHistory() throws Exception {
        HistoryResponse historyResponse = new HistoryResponse(SUCCESS);
        when(bitcoinHistoryService.serveHistoryForTimeRange(anyString(), anyString(), eq("INR"), anyBoolean()))
                .thenReturn(ResponseEntity.ok(historyResponse));

        mockMvc.perform(get("/bitcoin/history/dateRange")
                        .param("fromDate", "2024-01-01")
                        .param("toDate", "2024-12-31")
                        .param("targetCurrency", "USD")
                        .param("offlineMode", "false"))
                .andExpect(status().isOk());
    }
}
