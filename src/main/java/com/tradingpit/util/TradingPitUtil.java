package com.tradingpit.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingpit.dto.AffiliateTransactionsDTO;
import com.tradingpit.dto.SecondExternalApiDTO;
import com.tradingpit.mapper.AffiliateResourceDestinationMapper;
import com.tradingpit.model.AffiliateClientMap;
import com.tradingpit.model.AffiliateTransactions;
import com.tradingpit.model.FailedCalls;
import com.tradingpit.repository.AffiliateClientMapRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TradingPitUtil {

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private AffiliateClientMapRepository clientMapRepository;
	
	private final AffiliateResourceDestinationMapper affiliateToDestinationMapper;
	
	public FailedCalls createFailedCall(String ex, String payload, String clientId, String requestType) throws JsonProcessingException {
		FailedCalls failedCall = new FailedCalls();
		
		failedCall.setClientId(clientId);
		failedCall.setPayload(payload);
		failedCall.setProcessed(false);
		failedCall.setReasonOfFailure(ex);
		failedCall.setRequestType(requestType);
		failedCall.setFailureDate(LocalDateTime.now());
		
		return failedCall;
	}
	
	public JsonNode extractStringFromFile(String filePath) throws IOException {
		Resource resource = resourceLoader.getResource(filePath);
		String myString = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
		JsonNode jsonNode = mapper.readTree(myString);
		return jsonNode;
	}
	
	public AffiliateTransactions createTransactions(AffiliateTransactionsDTO affiliateTransactionsDTO, String filePath) {
		
		AffiliateTransactions transaction;
		JsonNode json = null;
		try {
			json = extractStringFromFile(filePath);
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		if(json == null) {
			throw new NullPointerException("Response file is null");
		}
		
		AffiliateClientMap clientMap = findAffiliateClientMap(affiliateTransactionsDTO);
		transaction = affiliateToDestinationMapper.transactionsDTOToTransactions(affiliateTransactionsDTO);
		
		transaction.setReferralCode(clientMap.getReferralCode());
		transaction.setConversionId(BigInteger.valueOf(json.get("id").asLong()));
		transaction.setConversionAmount(BigDecimal.valueOf(json.get("commissions").get(0).get("amount").asDouble()));
		transaction.setCurrency(json.get("commissions").get(0).get("currency").asText());
		transaction.setCreationDate(LocalDateTime.now());
		
		return transaction;
	}
	
	
	public SecondExternalApiDTO createSecondExternalApiDTO(AffiliateTransactionsDTO affiliateTransactionsDTO, String currency) {
		SecondExternalApiDTO secondExternalDTO;
		AffiliateClientMap clientMap = findAffiliateClientMap(affiliateTransactionsDTO);
		
		secondExternalDTO = affiliateToDestinationMapper.clientMapToSecondExternalDTO(clientMap);
		secondExternalDTO = affiliateToDestinationMapper.updateSecondExternalDTO(secondExternalDTO, affiliateTransactionsDTO);
		secondExternalDTO.setCurrency(currency);
		
		return secondExternalDTO;
	}

	public AffiliateClientMap findAffiliateClientMap(AffiliateTransactionsDTO affiliateTransactionsDTO) {
		Optional<AffiliateClientMap> clientMapList;
		AffiliateClientMap clientMap = null;
		clientMapList = clientMapRepository.findTopByClientIdOrderByCreationDateDesc(affiliateTransactionsDTO.getClientId());
		if(clientMapList.isPresent()) {
			clientMap = clientMapList.get();
		}
		return clientMap;
	}
	
}
