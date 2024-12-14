package com.ps.cryptohistory.template;

import com.ps.cryptohistory.exception.CryptoHistoryException;
import com.ps.cryptohistory.exception.NoDataFoundException;
import com.ps.cryptohistory.exception.ValidationException;
import com.ps.cryptohistory.model.HistoryResponse;
import com.ps.cryptohistory.utils.Util;
import com.ps.cryptohistory.model.HistoryRequest;
import com.ps.cryptohistory.service.PriceService;
import com.ps.cryptohistory.service.history.BitcoinHistoryQueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BitcoinHistoryServiceTest {

    @Mock
    private BitcoinHistoryQueryService bitcoinHistoryQueryService;

    @Mock
    private PriceService priceService;

    @InjectMocks
    private BitcoinHistoryService bitcoinHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateAndCreateRequest_ShouldReturnValidRequest() throws ValidationException {
        String fromDate = "2023-01-01";
        String toDate = "2023-01-31";
        String targetCurrency = "USD";
        boolean offlineMode = false;

        HistoryRequest historyRequest = bitcoinHistoryService.validateAndCreateRequest(fromDate, toDate, targetCurrency, offlineMode);

        assertNotNull(historyRequest);
        assertEquals(LocalDate.of(2023, 1, 1), historyRequest.getFromDate());
        assertEquals(LocalDate.of(2023, 1, 31), historyRequest.getToDate());
        assertEquals(targetCurrency, historyRequest.getTargetCurrency());
        assertFalse(historyRequest.isOfflineMode());
    }

    @Test
    void validateAndCreateRequest_ShouldThrowValidationException_WhenInvalidDate() {
        String fromDate = "invalid-date";
        String toDate = "2023-01-31";
        String targetCurrency = "USD";
        boolean offlineMode = false;

        ValidationException exception = assertThrows(ValidationException.class, () ->
                bitcoinHistoryService.validateAndCreateRequest(fromDate, toDate, targetCurrency, offlineMode));

        Assertions.assertEquals(Util.DATE_ERROR, exception.getOutputMessage());
    }

    @Test
    void getHistoryForDateRange_ShouldReturnHistory() throws NoDataFoundException {
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 1, 31);
        Map<String, BigDecimal> historyData = new HashMap<>();
        historyData.put("2023-01-01", BigDecimal.valueOf(1000));

        when(bitcoinHistoryQueryService.getHistoryForDateRange(fromDate, toDate)).thenReturn(historyData);

        HistoryRequest request = new HistoryRequest();
        request.setFromDate(fromDate);
        request.setToDate(toDate);
        Map<String, BigDecimal> result = bitcoinHistoryService.getHistoryForDateRange(request);

        assertEquals(historyData, result);
        verify(bitcoinHistoryQueryService, times(1)).getHistoryForDateRange(fromDate, toDate);
    }

    @Test
    void getHistoryForDateRange_ShouldThrowNoDataFoundException_WhenNoData() {
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 1, 31);

        when(bitcoinHistoryQueryService.getHistoryForDateRange(fromDate, toDate)).thenReturn(new HashMap<>());

        HistoryRequest request = new HistoryRequest();
        request.setFromDate(fromDate);
        request.setToDate(toDate);

        NoDataFoundException exception = assertThrows(NoDataFoundException.class, () ->
                bitcoinHistoryService.getHistoryForDateRange(request));

        Assertions.assertEquals(Util.NO_DATA_ERROR, exception.getOutputMessage());
        verify(bitcoinHistoryQueryService, times(1)).getHistoryForDateRange(fromDate, toDate);
    }

    @Test
    void buildHistoryResponse_ShouldReturnResponse() throws CryptoHistoryException, ParseException {
        Map<String, BigDecimal> historyData = new HashMap<>();
        historyData.put("2023-01-01", BigDecimal.valueOf(1000));

        HistoryRequest request = new HistoryRequest();
        request.setTargetCurrency("USD");

        HistoryResponse.Builder builder = new HistoryResponse.Builder();
        builder.targetCurrency("USD");
        builder.message("Success");

        doNothing().when(priceService).convertPrices(historyData, "USD", builder);

        HistoryResponse response = bitcoinHistoryService.buildHistoryResponse(historyData, request);

        assertNotNull(response);
        assertEquals("USD", response.getTargetCurrency());
        assertEquals("Success", response.getMessage());
        verify(priceService, times(1)).convertPrices(eq(historyData), eq("USD"), any(HistoryResponse.Builder.class));

    }

    @Test
    void buildHistoryResponse_ShouldThrowCryptoHistoryException_WhenParseException() throws ParseException {
        Map<String, BigDecimal> historyData = new HashMap<>();

        HistoryRequest request = new HistoryRequest();
        request.setTargetCurrency("USD");

        doThrow(ParseException.class).when(priceService)
                .convertPrices(eq(historyData), eq("USD"), any(HistoryResponse.Builder.class));

        CryptoHistoryException exception = assertThrows(CryptoHistoryException.class, () ->
                bitcoinHistoryService.buildHistoryResponse(historyData, request));

        Assertions.assertEquals(Util.INTERNAL_SERVER_ERROR, exception.getOutputMessage());
        verify(priceService, times(1)).convertPrices(eq(historyData), eq("USD"), any(HistoryResponse.Builder.class));
    }
}