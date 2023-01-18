package com.tradingpit.dto;

import java.math.BigDecimal;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AffiliateTransactionsDTO {

	@NotEmpty(message="First name is mandatory")
	private String orderId;
	
	@NotEmpty(message="Landing page is mandatory")
	private BigDecimal totalPrice;
	
	@NotEmpty(message="Referral code is mandatory")
	private String clientId;
	
	@NotEmpty(message="User agent is mandatory")
	private String transactionType;
	
	@NonNull
	private boolean successful;
	
}
