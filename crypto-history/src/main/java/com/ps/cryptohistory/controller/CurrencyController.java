package com.ps.cryptohistory.controller;

import com.ps.cryptohistory.service.cache.CurrencyCacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
@Log4j2
public class CurrencyController {

    CurrencyCacheService currencyCacheService;

    @Autowired
    public CurrencyController(CurrencyCacheService currencyCacheService) {
        this.currencyCacheService = currencyCacheService;
    }

    @GetMapping
    @Operation(summary = "Get list of all supported currencies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    }) public List<String> getAllCurrencies() {
        log.info("Fetching all currencies...");
        return currencyCacheService.getCurrencyList();
    }
}
