package com.ps.cryptohistory.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Data
@AllArgsConstructor
public class BitcoinHistoryCache {
    private ConcurrentHashMap<String, BigDecimal> history;

    public void updateMap(Map<String, BigDecimal> updatedHistory) {
        history.putAll(updatedHistory);
    }


}
