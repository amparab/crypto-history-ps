package com.publicissapient.cryptohistory.service.cache;

import com.publicissapient.cryptohistory.cache.ConversionRatesCache;
import com.publicissapient.cryptohistory.model.ConversionRates;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.publicissapient.cryptohistory.utils.Util.GET_CONVERSION_RATES;

@Qualifier("conversionRatesCacheService")
@Service
@Log4j2
public class ConversionRatesCacheService implements CacheService{

    private final RestTemplate restTemplate;

    private final ConversionRatesCache conversionRatesCache;

    @Value("${api_key}")
    private String apiKey;

    @Autowired
    public ConversionRatesCacheService(RestTemplate restTemplate,
                                       ConversionRatesCache conversionRatesCache) {
        this.restTemplate = restTemplate;
        this.conversionRatesCache = conversionRatesCache;
    }

    @PostConstruct
    @Override
    public void init() {
        updateCache();
        log.info("Conversion rates cache updated on startup with {} currencies.", conversionRatesCache.getConversionRates().size());
    }

    /*
        Updates conversion rates cache every 5 minutes
    */
    @Scheduled(fixedRate = 300000)
    @Override
    public void updateCache() {
        try {
            ConversionRates updatedRates = getConversionRates();
            conversionRatesCache.updateConversionRates(updatedRates.getConversionRates());
            log.info("Conversion rates cache updated successfully");
        } catch (Exception e) {
            log.error("Error updating conversion rates cache", e);
        }
    }

    private ConversionRates getConversionRates() {
        String url = GET_CONVERSION_RATES.replace("{apiKey}", apiKey);
        return restTemplate.getForObject(url, ConversionRates.class);
    }
}
