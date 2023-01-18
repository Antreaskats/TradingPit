package com.tradingpit.serviceImpl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.tradingpit.dto.AffiliateClientMapDTO;
import com.tradingpit.dto.AffiliateTransactionsDTO;
import com.tradingpit.exception.CallFailedException;
import com.tradingpit.mapper.SimpleResourceDestinationMapper;
import com.tradingpit.repository.AffiliateTransactionsRepository;
import com.tradingpit.repository.FailedCallsRepository;
import com.tradingpit.service.CallSecondExternalAPIService;
import com.tradingpit.util.TradingPitUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallSecondExternalAPIServiceImpl implements CallSecondExternalAPIService{

	@Autowired
	private FailedCallsRepository failedCallRepository;
	
	@Autowired
	private AffiliateTransactionsRepository transactionsRepository;
	
	@Autowired
	private TradingPitUtil util;
	
	private final SimpleResourceDestinationMapper sourceToDestinationMapper;
	
	private final String URI = "http://exercise/tap/conversions";
	
	@Override
	public void callFailService(AffiliateTransactionsDTO affiliateTransactionsDTO) throws ResourceAccessException {
		log.info("Retrying");
		RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.postForObject(URI, affiliateTransactionsDTO, String.class);
		
	}
	
	@Recover
    public void recover(ResourceAccessException ex, AffiliateClientMapDTO affiliateClientMapDTO) throws CallFailedException{
		log.info("Recovered");
		
		try {
			failedCallRepository.save(util.createFailedCall(ex.getMessage(), affiliateClientMapDTO));
		} catch (JsonProcessingException e) {
			log.info(e.getMessage());
		}
		
		throw new CallFailedException("Call to external API failed");
    }

	@Override
	public JsonNode callSuccessService(AffiliateTransactionsDTO affiliateTransactionsDTO) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
