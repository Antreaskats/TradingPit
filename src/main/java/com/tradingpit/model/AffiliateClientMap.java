package com.tradingpit.model;

import java.math.BigInteger;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AffiliateClientMap {

	@Id
	@GeneratedValue
	private BigInteger id;
	
	private String clientId;
	
	private String referralCode;
	
	private String clickId;
	
	private String userAgent;
	
}
