package com.publicissapient.cryptohistory.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
public class ConversionRatesCache {
    private ConcurrentHashMap<String, BigDecimal> conversionRates;
    public void updateConversionRates(Map<String, BigDecimal> updatedRates) {
        conversionRates.putAll(updatedRates);
    }
}
