package com.ps.cryptohistory.service;

import com.ps.cryptohistory.cache.ConversionRatesCache;
import com.ps.cryptohistory.model.HistoryResponse;
import com.ps.cryptohistory.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PriceServiceTest {

    @Mock
    private ConversionRatesCache conversionRatesCache;

    @InjectMocks
    private PriceService priceService;

    private HistoryResponse.Builder responseBuilder;
    private Map<String, BigDecimal> bitcoinHistoryForDateRange;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        responseBuilder = new HistoryResponse.Builder();

        bitcoinHistoryForDateRange = new HashMap<>();
        bitcoinHistoryForDateRange.put("2024-12-01", new BigDecimal("30000"));
        bitcoinHistoryForDateRange.put("2024-12-02", new BigDecimal("31000"));
        bitcoinHistoryForDateRange.put("2024-12-03", new BigDecimal("32000"));

        ConcurrentHashMap<String, BigDecimal> conversionRates = new ConcurrentHashMap<>();
        conversionRates.put("EUR", new BigDecimal("0.85"));
        conversionRates.put("GBP", new BigDecimal("0.75"));
        when(conversionRatesCache.getConversionRates()).thenReturn(conversionRates);
    }

    @Test
    void testConvertPrices() throws ParseException {
        String targetCurrency = "EUR";

        priceService.convertPrices(bitcoinHistoryForDateRange, targetCurrency, responseBuilder);
        HistoryResponse response = responseBuilder.build();

        assertEquals(new BigDecimal("32000"), response.getHighestValue());
        assertEquals(new BigDecimal("30000"), response.getLowestValue());

        List<Price> priceHistory = response.getPriceHistory();
        assertEquals(3, priceHistory.size());
    }

}
