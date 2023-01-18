package com.tradingpit.model;

import java.math.BigInteger;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FailedCalls {
	
	@Id
	@GeneratedValue
	private BigInteger id;
	
	private String clientId;
	
	private String requestType;
	
	private String payload;

	private String reasonOfFailure;
	
	private Boolean processed;
	
}
