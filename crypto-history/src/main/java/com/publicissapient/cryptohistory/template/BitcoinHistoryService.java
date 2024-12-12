package com.publicissapient.cryptohistory.template;

import com.publicissapient.cryptohistory.exception.CryptoHistoryException;
import com.publicissapient.cryptohistory.exception.NoDataFoundException;
import com.publicissapient.cryptohistory.exception.ValidationException;
import com.publicissapient.cryptohistory.model.HistoryRequest;
import com.publicissapient.cryptohistory.model.HistoryResponse;
import com.publicissapient.cryptohistory.service.history.BitcoinHistoryQueryService;
import com.publicissapient.cryptohistory.service.PriceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static com.publicissapient.cryptohistory.utils.Util.*;

@Service
@Log4j2
public class BitcoinHistoryService extends HistoryService {

    private final BitcoinHistoryQueryService bitcoinHistoryQueryService;
    private final PriceService priceService;
    @Autowired
    public BitcoinHistoryService(BitcoinHistoryQueryService bitcoinHistoryQueryService, PriceService priceService) {
        this.bitcoinHistoryQueryService = bitcoinHistoryQueryService;
        this.priceService = priceService;
    }

    @Override
    public HistoryRequest validateAndCreateRequest(String fromDate,
                                                   String toDate,
                                                   String targetCurrency,
                                                   boolean offlineMode) throws ValidationException {
        try {
            HistoryRequest historyRequest = new HistoryRequest();
            historyRequest.setFromDate(getLocalDateFromString(fromDate));
            historyRequest.setToDate(getLocalDateFromString(toDate));
            historyRequest.setTargetCurrency(targetCurrency);
            historyRequest.setOfflineMode(offlineMode);
            return historyRequest;
        } catch (DateTimeParseException dateTimeParseException){
            log.error("Exception occured while parsing dates. Request :: fromDate: {}, toDate: {}, currency: {}", fromDate, toDate, targetCurrency);
            throw new ValidationException(DATE_ERROR);
        }

    }

    @Override
    public Map<String, BigDecimal> getHistoryForDateRange(HistoryRequest historyRequest) throws NoDataFoundException {
        Map<String, BigDecimal> bitcoinHistoryForDateRange = bitcoinHistoryQueryService
                .getHistoryForDateRange(historyRequest.getFromDate(), historyRequest.getToDate());
        if(bitcoinHistoryForDateRange.isEmpty()){
            log.info("No history data found. Request :: fromDate: {}, toDate: {}, currency: {}", historyRequest.getFromDate(), historyRequest.getToDate(), historyRequest.getTargetCurrency());
            throw new NoDataFoundException(NO_DATA_ERROR);
        }
        return bitcoinHistoryForDateRange;
    }

    @Override
    public HistoryResponse buildHistoryResponse(Map<String, BigDecimal> historyForDateRangeMap, HistoryRequest historyRequest) throws CryptoHistoryException {
        try {
            HistoryResponse.Builder responseBuilder = new HistoryResponse.Builder();
            responseBuilder.targetCurrency(historyRequest.getTargetCurrency());
            priceService.convertPrices(historyForDateRangeMap, historyRequest.getTargetCurrency(), responseBuilder);
            responseBuilder.message(SUCCESS);
            return responseBuilder.build();
        } catch (ParseException parseException){
            log.error("Error while occured while parsing. Request :: fromDate: {}, toDate: {}, currency: {}",
                    historyRequest.getFromDate(), historyRequest.getToDate(), historyRequest.getTargetCurrency(), parseException);
            throw new CryptoHistoryException(INTERNAL_SERVER_ERROR);
        }
    }
}
