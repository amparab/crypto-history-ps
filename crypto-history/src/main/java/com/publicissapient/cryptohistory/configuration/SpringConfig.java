package com.publicissapient.cryptohistory.configuration;

import com.publicissapient.cryptohistory.cache.BitcoinHistoryCache;
import com.publicissapient.cryptohistory.cache.ConversionRatesCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SpringConfig {
    @Bean
    public ConversionRatesCache conversionRatesCache() {
        return new ConversionRatesCache(new ConcurrentHashMap<>());
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public BitcoinHistoryCache bitcoinHistoryCache() {
        return new BitcoinHistoryCache(new ConcurrentHashMap<>());
    }
    @Bean
    public List<String> currencyCache() {
        return new ArrayList<>();
    }



}
