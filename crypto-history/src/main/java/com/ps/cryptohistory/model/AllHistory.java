package com.ps.cryptohistory.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
public class AllHistory {
    private ConcurrentHashMap<String, BigDecimal> bpi;
}
