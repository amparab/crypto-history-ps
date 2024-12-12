package com.publicissapient.cryptohistory.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HistoryRequest {

    private LocalDate fromDate;
    private LocalDate toDate;
    private String targetCurrency;
    private boolean offlineMode;

}
