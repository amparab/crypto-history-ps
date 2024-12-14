package com.ps.cryptohistory.service.cache;

import com.ps.cryptohistory.cache.BitcoinHistoryCache;
import com.ps.cryptohistory.model.AllHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BitcoinHistoryCacheServiceTest {

    @InjectMocks
    private BitcoinHistoryCacheService bitcoinHistoryCacheService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BitcoinHistoryCache bitcoinHistoryCache;

    @Mock
    private AllHistory allHistory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateBitcoinHistory_Success() {
        when(restTemplate.getForObject(anyString(), eq(AllHistory.class))).thenReturn(allHistory);
        doNothing().when(bitcoinHistoryCache).updateMap(any());
        bitcoinHistoryCacheService.updateCache();
        verify(bitcoinHistoryCache, times(1)).updateMap(any());
    }

    @Test
    public void testUpdateBitcoinHistory_Failure() {
        when(restTemplate.getForObject(anyString(), eq(AllHistory.class)))
                .thenThrow(new RuntimeException("API call failed"));
        bitcoinHistoryCacheService.updateCache();
        verify(bitcoinHistoryCache, times(0)).updateMap(any());
    }
}
