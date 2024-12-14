package com.ps.cryptohistory.service.history;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface HistoryQueryService {

    Map<String, BigDecimal> getHistoryForDateRange(LocalDate fromDate, LocalDate toDate);

}
