package com.tradingpit.serviceImpl;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradingpit.dto.AffiliateClientMapDTO;
import com.tradingpit.exception.CallFailedException;
import com.tradingpit.service.AffiliateClientMapService;
import com.tradingpit.service.CallExternalAPIService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AffiliateClientMapServiceImpl implements AffiliateClientMapService{
	
	@Autowired
	private CallExternalAPIService callExternalService;

	@Override
	public JsonNode callClicks(AffiliateClientMapDTO affiliateClientMapDTO) throws IOException {
		if (!affiliateClientMapDTO.isSuccessful()) {
			try {
				callExternalService.callFailService(affiliateClientMapDTO);
			} catch (CallFailedException e) {
				log.info("Caught exception " + e.getMessage());
				throw new CallFailedException(e.getMessage());
			}
		}
		JsonNode clickId = callExternalService.callSuccessService(affiliateClientMapDTO);

		return clickId;
		
	}
	


}
