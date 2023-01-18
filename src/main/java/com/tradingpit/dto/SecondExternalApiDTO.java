package com.tradingpit.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SecondExternalApiDTO {

	private String clickId;
	
	private String externalId;
	
	private BigDecimal amount;
	
	private String currency;
	
	private String customerId;
	
	private String user_agent;
	
	private String ip;
	
}
