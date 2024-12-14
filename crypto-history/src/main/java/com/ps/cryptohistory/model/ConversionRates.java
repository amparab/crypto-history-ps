package com.ps.cryptohistory.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ConversionRates {
    @JsonProperty("conversion_rates")
    private Map<String, BigDecimal> conversionRates;
}
