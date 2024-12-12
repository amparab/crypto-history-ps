package com.publicissapient.cryptohistory.service.history;

import com.publicissapient.cryptohistory.cache.BitcoinHistoryCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BitcoinHistoryQueryServiceTest {

    @Mock
    private BitcoinHistoryCache bitcoinHistoryCache;

    @InjectMocks
    private BitcoinHistoryQueryService bitcoinHistoryQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ConcurrentHashMap<String, BigDecimal> bitcoinHistoryData = new ConcurrentHashMap<>();
        bitcoinHistoryData.put("2024-12-01", new BigDecimal("30000"));
        bitcoinHistoryData.put("2024-12-02", new BigDecimal("31000"));
        bitcoinHistoryData.put("2024-12-03", new BigDecimal("32000"));

        when(bitcoinHistoryCache.getHistory()).thenReturn(bitcoinHistoryData);
    }

    @Test
    void testGetHistoryForDateRange() {
        LocalDate fromDate = LocalDate.parse("2024-12-01");
        LocalDate toDate = LocalDate.parse("2024-12-02");

        Map<String, BigDecimal> result = bitcoinHistoryQueryService.getHistoryForDateRange(fromDate, toDate);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("2024-12-01"));
        assertTrue(result.containsKey("2024-12-02"));
        assertFalse(result.containsKey("2024-12-03"));
    }
    @Test
    void testGetHistoryForDateRangeNoResults() {
        LocalDate fromDate = LocalDate.parse("2024-12-04");
        LocalDate toDate = LocalDate.parse("2024-12-05");

        Map<String, BigDecimal> result = bitcoinHistoryQueryService.getHistoryForDateRange(fromDate, toDate);

        assertTrue(result.isEmpty());
    }
}
