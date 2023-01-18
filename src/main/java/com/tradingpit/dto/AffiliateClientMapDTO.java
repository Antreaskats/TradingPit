package com.tradingpit.dto;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AffiliateClientMapDTO {

	@NotEmpty(message="First name is mandatory")
	private String clientId;
	
	@NotEmpty(message="Landing page is mandatory")
	private String landingPage;
	
	@NotEmpty(message="Referral code is mandatory")
	private String referralCode;
	
	@NotEmpty(message="User agent is mandatory")
	private String userAgent;

	@NotEmpty(message="IP is mandatory")
	private String ip;
	
	@NonNull
	private boolean successful;
}
