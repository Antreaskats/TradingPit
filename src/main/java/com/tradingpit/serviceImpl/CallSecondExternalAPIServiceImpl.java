package com.tradingpit.serviceImpl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingpit.dto.AffiliateTransactionsDTO;
import com.tradingpit.dto.SecondExternalApiDTO;
import com.tradingpit.exception.CallFailedException;
import com.tradingpit.model.AffiliateTransactions;
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
	
	@Autowired
	private ObjectMapper mapper;
	
	
	@Value("${file.secondFile}")
	private String filePath;
	
	private final String URI = "http://exercise/tap/conversions";
	
	@Override
	public void callFailService(AffiliateTransactionsDTO affiliateTransactionsDTO, SecondExternalApiDTO secondExternalDTO) throws ResourceAccessException {
		log.info("Retrying");
		RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.postForObject(URI, secondExternalDTO, String.class);
	    log.info(result);
	}
	
	@Recover
    public void recover(ResourceAccessException ex, AffiliateTransactionsDTO affiliateTransactionsDTO) throws CallFailedException{
		log.info("Recovered");
		
		try {
			failedCallRepository.save(util.createFailedCall(ex.getMessage(), mapper.writeValueAsString(affiliateTransactionsDTO), affiliateTransactionsDTO.getClientId(), "createConversion"));
		} catch (JsonProcessingException e) {
			log.info(e.getMessage());
		}
		
		throw new CallFailedException("Call to external API failed");
    }

	@Override
	public JsonNode callSuccessService(AffiliateTransactionsDTO affiliateTransactionsDTO) throws IOException {
		JsonNode json;
		
		try {
			json = util.extractStringFromFile(filePath);
		} catch (IOException e) {
			log.info("IOException caught "+ e.getMessage());
			throw new IOException(e.getMessage());
		}
		
		AffiliateTransactions affiliateTransaction = util.createTransactions(affiliateTransactionsDTO, filePath);
		
		affiliateTransaction = transactionsRepository.save(affiliateTransaction);
		
		return json;
	}
}
