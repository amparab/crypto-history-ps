package com.ps.cryptohistory.service;

import com.ps.cryptohistory.model.HistoryResponse;
import com.ps.cryptohistory.model.Price;
import com.ps.cryptohistory.utils.Util;
import com.ps.cryptohistory.cache.ConversionRatesCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class PriceService {

    private final ConversionRatesCache conversionRatesCache;

    @Autowired
    public PriceService(ConversionRatesCache conversionRatesCache) {
        this.conversionRatesCache = conversionRatesCache;
    }
    
    public void convertPrices(Map<String, BigDecimal> bitcoinHistoryForDateRange,
                              String targetCurrency,
                              HistoryResponse.Builder responseBuilder) throws ParseException {

        BigDecimal highestValue = Collections.max(bitcoinHistoryForDateRange.values());
        BigDecimal lowestValue = Collections.min(bitcoinHistoryForDateRange.values());
        responseBuilder.highestValue(highestValue);
        responseBuilder.lowestValue(lowestValue);
        responseBuilder.priceHistory(getPriceList(bitcoinHistoryForDateRange, highestValue, lowestValue, targetCurrency));
    }

    private List<Price> getPriceList(Map<String, BigDecimal> bitcoinHistoryForDateRange,
                                     BigDecimal highestValue,
                                     BigDecimal lowestValue,
                                     String targetCurrency) throws ParseException {

        List<Price> convertedPriceHistory = new ArrayList<>();

        for(Map.Entry<String, BigDecimal> entry : bitcoinHistoryForDateRange.entrySet()){
            Price price = new Price();
            price.setDate(Util.formatDate(entry.getKey()));
            price.setUsdValue(checkValue(entry.getValue(), highestValue, lowestValue));
            price.setTargetValue(getConvertedAmount(targetCurrency, entry.getValue()).toString());
            convertedPriceHistory.add(price);
        }

        return convertedPriceHistory;
    }

    private String checkValue(BigDecimal currentValue, BigDecimal highestValue, BigDecimal lowestValue) {
        if(currentValue.compareTo(highestValue) == 0)
            return currentValue + Util.HIGH_VALUE_TEXT;
        if(currentValue.compareTo(lowestValue) == 0)
            return currentValue + Util.LOW_VALUE_TEXT;
        return currentValue.toString();
    }

    private BigDecimal getConvertedAmount(String targetCurrency, BigDecimal usdPrice){
        if(targetCurrency.equals(Util.USD_CURRENCY))
            return usdPrice;
        BigDecimal conversionRate = conversionRatesCache.getConversionRates().get(targetCurrency);
        return usdPrice.multiply(conversionRate);
    }
}
