package com.ps.cryptohistory.service.history;

import com.ps.cryptohistory.cache.BitcoinHistoryCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BitcoinHistoryQueryService implements HistoryQueryService {
    private final BitcoinHistoryCache bitcoinHistoryCache;

    @Autowired
    public BitcoinHistoryQueryService(BitcoinHistoryCache bitcoinHistoryCache) {
        this.bitcoinHistoryCache = bitcoinHistoryCache;
    }


    @Override
    public Map<String, BigDecimal> getHistoryForDateRange(LocalDate fromDate, LocalDate toDate) {
        return bitcoinHistoryCache.getHistory().entrySet().stream()
                .filter(entry -> {
                    LocalDate entryDate = LocalDate.parse(entry.getKey());
                    return !entryDate.isBefore(fromDate) && !entryDate.isAfter(toDate);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
