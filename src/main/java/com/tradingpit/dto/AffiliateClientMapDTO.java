package com.tradingpit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AffiliateClientMapDTO {

	@NotEmpty(message="Client Id is mandatory and must be a UUID")
	@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
	private String clientId;
	
	@NotEmpty(message="Landing page is mandatory")
	private String landingPage;
	
	@NotEmpty(message="Referral code is mandatory")
	private String referralCode;
	
	@NotEmpty(message="User agent is mandatory")
	private String userAgent;

	@NotEmpty(message="IP is mandatory")
	private String ip;
}
