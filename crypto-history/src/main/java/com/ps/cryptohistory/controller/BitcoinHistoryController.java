package com.ps.cryptohistory.controller;

import com.ps.cryptohistory.model.HistoryResponse;
import com.ps.cryptohistory.template.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bitcoin")
@Log4j2
public class BitcoinHistoryController {


    HistoryService bitcoinHistoryService;

    @Autowired
    public BitcoinHistoryController(HistoryService bitcoinHistoryService) {
        this.bitcoinHistoryService = bitcoinHistoryService;
    }

    @GetMapping("/history/dateRange")
    @Operation(summary = "Get bitcoin price history for a given date range in desired currency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HistoryResponse> getBitcoinHistoryForTimeRange(
            @Parameter(description = "Date from which price history should be fetched", example = "2024-11-03")
            @RequestParam String fromDate,
            @Parameter(description = "Date to which price history should be fetched", example = "2024-11-15")
            @RequestParam String toDate,
            @Parameter(description = "Currency in which prices should be displayed", example = "INR")
            @RequestParam(defaultValue = "USD") String targetCurrency,
            @Parameter(description = "Online mode / Offline mode", example = "true")
            @RequestParam(defaultValue = "false") boolean offlineMode) {
        return bitcoinHistoryService.serveHistoryForTimeRange(fromDate, toDate, targetCurrency, offlineMode);
    }
}
