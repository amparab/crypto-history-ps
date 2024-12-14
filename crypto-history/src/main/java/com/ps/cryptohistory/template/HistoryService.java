package com.ps.cryptohistory.template;

import com.ps.cryptohistory.exception.CryptoHistoryException;
import com.ps.cryptohistory.exception.NoDataFoundException;
import com.ps.cryptohistory.exception.ValidationException;
import com.ps.cryptohistory.model.HistoryRequest;
import com.ps.cryptohistory.model.HistoryResponse;
import com.ps.cryptohistory.utils.Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

@Log4j2
public abstract class HistoryService {

    public final ResponseEntity<HistoryResponse> serveHistoryForTimeRange(String fromDate,
                                                                          String toDate,
                                                                          String targetCurrency,
                                                                          boolean offlineMode) {
        log.info("In getBitcoinHistory controller. Request :: fromDate: {}, toDate: {}, currency: {}", fromDate, toDate, targetCurrency);
        try {
            HistoryRequest request = validateAndCreateRequest(fromDate, toDate, targetCurrency, offlineMode);
            Map<String, BigDecimal> historyForDateRangeMap = getHistoryForDateRange(request);
            HistoryResponse historyResponse = buildHistoryResponse(historyForDateRangeMap, request);
            log.info(". Request :: fromDate: {}, toDate: {}, currency: {} processed successfully", fromDate, toDate, targetCurrency);
            return ResponseEntity.ok(historyResponse);
        } catch (NoDataFoundException noDataFoundException) {
            return ResponseEntity.ok(createFailureResponse(noDataFoundException.getOutputMessage()));
        } catch (ValidationException validationException){
            return ResponseEntity.badRequest().body(createFailureResponse(validationException.getOutputMessage()));
        } catch (Exception exception){
            log.error("Exception occured! Request :: fromDate: {}, toDate: {}, currency: {}", fromDate, toDate, targetCurrency);
            return ResponseEntity.internalServerError().body(createFailureResponse(Util.INTERNAL_SERVER_ERROR));
        }

    }

    public abstract HistoryRequest validateAndCreateRequest(String fromDate,
                                                String toDate,
                                                String targetCurrency,
                                                boolean offlineMode) throws Exception;

    public abstract Map<String, BigDecimal> getHistoryForDateRange(HistoryRequest historyRequest) throws CryptoHistoryException, NoDataFoundException;
    public abstract HistoryResponse buildHistoryResponse(Map<String, BigDecimal> historyForDateRangeMap, HistoryRequest historyRequest) throws CryptoHistoryException, ParseException;

    public HistoryResponse createFailureResponse(String message){
        return new HistoryResponse(message);
    }
}
