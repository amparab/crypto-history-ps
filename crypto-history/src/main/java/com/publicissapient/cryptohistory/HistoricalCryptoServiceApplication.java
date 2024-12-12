package com.publicissapient.cryptohistory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HistoricalCryptoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HistoricalCryptoServiceApplication.class, args);
	}

}
