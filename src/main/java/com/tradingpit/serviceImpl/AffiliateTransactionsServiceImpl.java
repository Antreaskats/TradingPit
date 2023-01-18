package com.tradingpit.serviceImpl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradingpit.dto.AffiliateTransactionsDTO;
import com.tradingpit.exception.CallFailedException;
import com.tradingpit.service.AffiliateTransactionsService;
import com.tradingpit.service.CallSecondExternalAPIService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AffiliateTransactionsServiceImpl implements AffiliateTransactionsService{

	@Autowired
	private CallSecondExternalAPIService callSecondExternalServiceAPI;

	@Override
	public JsonNode callConversion(AffiliateTransactionsDTO affiliateTransactionsDTO) throws IOException {
		if (!affiliateTransactionsDTO.isSuccessful()) {
			try {
				callSecondExternalServiceAPI.callFailService(affiliateTransactionsDTO);
			} catch (CallFailedException e) {
				log.info("Caught exception " + e.getMessage());
				throw new CallFailedException(e.getMessage());
			}
		}
		JsonNode clickId = callSecondExternalServiceAPI.callSuccessService(affiliateTransactionsDTO);

		return clickId;
		
	}

}
