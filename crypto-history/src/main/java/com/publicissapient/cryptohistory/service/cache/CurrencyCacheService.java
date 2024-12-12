package com.publicissapient.cryptohistory.service.cache;

import com.publicissapient.cryptohistory.cache.ConversionRatesCache;
import com.publicissapient.cryptohistory.model.Currency;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.publicissapient.cryptohistory.utils.Util.GET_CURRENCIES;

@DependsOn("conversionRatesCacheService")
@Service
@Log4j2
public class CurrencyCacheService implements CacheService {

    private final RestTemplate restTemplate;
    private final List<String> currencyCache;
    private final ConversionRatesCache conversionRatesCache;

    @Autowired
    public CurrencyCacheService(RestTemplate restTemplate, List<String> currencyCache, ConversionRatesCache conversionRatesCache) {
        this.restTemplate = restTemplate;
        this.currencyCache = currencyCache;
        this.conversionRatesCache = conversionRatesCache;
    }

    @PostConstruct
    public void init() {
        updateCache();
        log.info("Fetched and loaded {} currencies", currencyCache.size());
    }

    @Override
    public void updateCache(){
        List<String> currencyList = getCurrencyList();
        currencyCache.addAll(currencyList);
        log.info("Fetched and loaded {} currencies", currencyList.size());
    }

    public List<String> getCurrencyList(){
        log.info("Fetching currencies from external API ...");
        Currency[] currencyArray = restTemplate.getForObject(GET_CURRENCIES, Currency[].class);
        return Arrays.stream(currencyArray)
                .map(Currency::getCurrency)
                .filter(c -> conversionRatesCache.getConversionRates().containsKey(c))
                .toList();
    }
}
