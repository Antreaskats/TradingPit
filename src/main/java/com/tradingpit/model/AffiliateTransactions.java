package com.tradingpit.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AffiliateTransactions {
	
	@Id
	@GeneratedValue
	private BigInteger id;
	
	private BigInteger conversionId;
	
	private String clientId;
	
	private String referralCode;
	
	private String orderId;
	
	private String currency;
	
	private BigDecimal orderAmount;
	
	private BigDecimal conversionAmount;
	
	private String transactionType;

	private LocalDateTime creationDate;
}
