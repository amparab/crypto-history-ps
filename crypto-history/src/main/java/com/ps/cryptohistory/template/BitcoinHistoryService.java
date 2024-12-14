package com.ps.cryptohistory.template;

import com.ps.cryptohistory.exception.CryptoHistoryException;
import com.ps.cryptohistory.exception.NoDataFoundException;
import com.ps.cryptohistory.exception.ValidationException;
import com.ps.cryptohistory.model.HistoryRequest;
import com.ps.cryptohistory.model.HistoryResponse;
import com.ps.cryptohistory.service.PriceService;
import com.ps.cryptohistory.utils.Util;
import com.ps.cryptohistory.service.history.BitcoinHistoryQueryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.Map;

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
            historyRequest.setFromDate(Util.getLocalDateFromString(fromDate));
            historyRequest.setToDate(Util.getLocalDateFromString(toDate));
            historyRequest.setTargetCurrency(targetCurrency);
            historyRequest.setOfflineMode(offlineMode);
            return historyRequest;
        } catch (DateTimeParseException dateTimeParseException){
            log.error("Exception occured while parsing dates. Request :: fromDate: {}, toDate: {}, currency: {}", fromDate, toDate, targetCurrency);
            throw new ValidationException(Util.DATE_ERROR);
        }

    }

    @Override
    public Map<String, BigDecimal> getHistoryForDateRange(HistoryRequest historyRequest) throws NoDataFoundException {
        Map<String, BigDecimal> bitcoinHistoryForDateRange = bitcoinHistoryQueryService
                .getHistoryForDateRange(historyRequest.getFromDate(), historyRequest.getToDate());
        if(bitcoinHistoryForDateRange.isEmpty()){
            log.info("No history data found. Request :: fromDate: {}, toDate: {}, currency: {}", historyRequest.getFromDate(), historyRequest.getToDate(), historyRequest.getTargetCurrency());
            throw new NoDataFoundException(Util.NO_DATA_ERROR);
        }
        return bitcoinHistoryForDateRange;
    }

    @Override
    public HistoryResponse buildHistoryResponse(Map<String, BigDecimal> historyForDateRangeMap, HistoryRequest historyRequest) throws CryptoHistoryException {
        try {
            HistoryResponse.Builder responseBuilder = new HistoryResponse.Builder();
            responseBuilder.targetCurrency(historyRequest.getTargetCurrency());
            priceService.convertPrices(historyForDateRangeMap, historyRequest.getTargetCurrency(), responseBuilder);
            responseBuilder.message(Util.SUCCESS);
            return responseBuilder.build();
        } catch (ParseException parseException){
            log.error("Error while occured while parsing. Request :: fromDate: {}, toDate: {}, currency: {}",
                    historyRequest.getFromDate(), historyRequest.getToDate(), historyRequest.getTargetCurrency(), parseException);
            throw new CryptoHistoryException(Util.INTERNAL_SERVER_ERROR);
        }
    }
}
