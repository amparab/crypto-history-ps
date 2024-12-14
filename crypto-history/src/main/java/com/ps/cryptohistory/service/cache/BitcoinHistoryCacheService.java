package com.ps.cryptohistory.service.cache;

import com.ps.cryptohistory.cache.BitcoinHistoryCache;
import com.ps.cryptohistory.utils.Util;
import com.ps.cryptohistory.model.AllHistory;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class BitcoinHistoryCacheService implements CacheService {

    private final RestTemplate restTemplate;

    private final BitcoinHistoryCache bitcoinHistoryCache;

    @Autowired
    public BitcoinHistoryCacheService(RestTemplate restTemplate,
                                 BitcoinHistoryCache bitcoinHistoryCache) {
        this.restTemplate = restTemplate;
        this.bitcoinHistoryCache = bitcoinHistoryCache;
    }

    @PostConstruct
    @Override
    public void init() {
        updateCache();
        log.info("Bitcoin history cache updated on startup");
    }

    /*
        Updates bitcoin history cache every 12 hours
    */
    @Override
    @Scheduled(cron = "0 0 0/12 * * ?")
    public void updateCache() {
        try {
            AllHistory updatedHistory = restTemplate.getForObject(Util.GET_HISTORY_URL, AllHistory.class);
            bitcoinHistoryCache.updateMap(updatedHistory.getBpi());
            log.info("Bitcoin history cache updated successfully");
        } catch (Exception e) {
            log.error("Error updating bitcoin history cache", e);
        }
    }
}
