package com.tradingpit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry(proxyTargetClass = true)
@SpringBootApplication
public class TradingPitApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingPitApplication.class, args);
	}

}
