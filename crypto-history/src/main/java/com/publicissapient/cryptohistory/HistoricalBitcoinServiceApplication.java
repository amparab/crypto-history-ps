package com.publicissapient.cryptohistory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HistoricalBitcoinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HistoricalBitcoinServiceApplication.class, args);
	}

}
