package com.publicissapient.cryptohistory.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class HistoryResponse {

    private final String message;
    private List<Price> priceHistory;
    private String targetCurrency;
    private BigDecimal highestValue;
    private BigDecimal lowestValue;

    private HistoryResponse(Builder builder) {
        this.priceHistory = builder.priceHistory;
        this.targetCurrency = builder.targetCurrency;
        this.highestValue = builder.highestValue;
        this.lowestValue = builder.lowestValue;
        this.message = builder.message;
    }

    public HistoryResponse(String message) {
        this.message = message;
    }

    public static class Builder {

        private String message;
        private List<Price> priceHistory;
        private String targetCurrency;
        private BigDecimal highestValue;
        private BigDecimal lowestValue;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder priceHistory(List<Price> priceHistory) {
            this.priceHistory = priceHistory;
            return this;
        }

        public Builder targetCurrency(String targetCurrency) {
            this.targetCurrency = targetCurrency;
            return this;
        }

        public Builder highestValue(BigDecimal highestValue) {
            this.highestValue = highestValue;
            return this;
        }

        public Builder lowestValue(BigDecimal lowestValue) {
            this.lowestValue = lowestValue;
            return this;
        }

        public HistoryResponse build() {
            return new HistoryResponse(this);
        }
    }
}
