package com.tradingpit.service;

import java.io.IOException;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradingpit.dto.AffiliateClientMapDTO;


public interface CallExternalAPIService {

	@Retryable(retryFor = {ResourceAccessException.class, HttpClientErrorException.class}, backoff= @Backoff(1000), maxAttempts = 3)
	public void callFailService(AffiliateClientMapDTO affiliateClientMapDTO) throws ResourceAccessException;
	
	public JsonNode callSuccessService(AffiliateClientMapDTO affiliateClientMapDTO) throws IOException;
	
}
