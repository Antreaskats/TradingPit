package com.tradingpit.dto;

import java.math.BigDecimal;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AffiliateTransactionsDTO {

	@NotEmpty(message="Order Id is mandatory and must be a UUID")
	@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
	private String orderId;
	
	@NonNull
	private BigDecimal totalPrice;
	
	@NotEmpty(message="Client Id is mandatory and must be a UUID")
	@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
	private String clientId;
	
	@Pattern(regexp = "(New|Reset|Extend)", message = "The transaction type can only take values: New, Reset and Extend")
	private String transactionType;
	
	@NonNull
	private boolean successful;
	
}
