package com.tradingpit.serviceImpl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradingpit.dto.AffiliateTransactionsDTO;
import com.tradingpit.dto.SecondExternalApiDTO;
import com.tradingpit.exception.CallFailedException;
import com.tradingpit.service.AffiliateTransactionsService;
import com.tradingpit.service.CallSecondExternalAPIService;
import com.tradingpit.util.TradingPitUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AffiliateTransactionsServiceImpl implements AffiliateTransactionsService{

	@Autowired
	private CallSecondExternalAPIService callSecondExternalServiceAPI;
	
	@Autowired
	private TradingPitUtil util;
	
	@Value("${transaction.currency}")
	private String currency;

	@Override
	public JsonNode callConversion(AffiliateTransactionsDTO affiliateTransactionsDTO) throws IOException {
		if (!affiliateTransactionsDTO.isSuccessful()) {
			SecondExternalApiDTO secondExternalApiDTO = util.createSecondExternalApiDTO(affiliateTransactionsDTO, currency);
			try {
				callSecondExternalServiceAPI.callFailService(affiliateTransactionsDTO, secondExternalApiDTO);
			} catch (CallFailedException e) {
				log.info("Caught exception " + e.getMessage());
				throw new CallFailedException(e.getMessage());
			}
		}
		JsonNode jsonResponse = callSecondExternalServiceAPI.callSuccessService(affiliateTransactionsDTO);

		return jsonResponse;
		
	}
	
	

}
