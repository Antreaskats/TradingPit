package com.tradingpit.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingpit.dto.AffiliateClientMapDTO;
import com.tradingpit.model.FailedCalls;

@Component
public class TradingPitUtil {

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	public FailedCalls createFailedCall(String ex, AffiliateClientMapDTO affiliateClientMapDTO) throws JsonProcessingException {
		FailedCalls failedCall = new FailedCalls();
		
		failedCall.setClientId(affiliateClientMapDTO.getClientId());
		failedCall.setPayload(mapper.writeValueAsString(affiliateClientMapDTO));
		failedCall.setProcessed(false);
		failedCall.setReasonOfFailure(ex);
		failedCall.setRequestType("createClick");
		
		return failedCall;
	}
	
	public JsonNode extractUUIDFromFile() throws IOException {
		Resource resource = resourceLoader.getResource("classpath:FirstExternalApiResponse.txt");
		String myString = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
		JsonNode jsonNode = mapper.readTree(myString);
		return jsonNode;
	}
	
}
