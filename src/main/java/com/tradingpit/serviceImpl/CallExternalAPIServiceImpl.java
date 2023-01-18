package com.tradingpit.serviceImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingpit.dto.AffiliateClientMapDTO;
import com.tradingpit.exception.CallFailedException;
import com.tradingpit.mapper.SimpleResourceDestinationMapper;
import com.tradingpit.model.AffiliateClientMap;
import com.tradingpit.model.FailedCalls;
import com.tradingpit.repository.AffiliateClientMapRepository;
import com.tradingpit.repository.FailedCallsRepository;
import com.tradingpit.service.CallExternalAPIService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CallExternalAPIServiceImpl implements CallExternalAPIService{
	
	@Autowired
	private FailedCallsRepository failedCallRepository;
	
	@Autowired
	private AffiliateClientMapRepository affiliateClientMapRepository;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private ObjectMapper mapper;
	
	private final SimpleResourceDestinationMapper sourceToDestinationMapper;
	
	@Override
	public void callFailService(AffiliateClientMapDTO affiliateClientMapDTO) throws ResourceAccessException{
		log.info("Retrying");
		RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.postForObject(affiliateClientMapDTO.getLandingPage(), affiliateClientMapDTO, String.class);
	}
	
	@Recover
    public void recover(ResourceAccessException ex, AffiliateClientMapDTO affiliateClientMapDTO) throws CallFailedException{
		log.info("Recovered");
		
		try {
			failedCallRepository.save(createFailedCall(ex.getMessage(), affiliateClientMapDTO));
		} catch (JsonProcessingException e) {
			log.info(e.getMessage());
		}
		
		throw new CallFailedException("Call to external API failed");
    }
	
	@Recover
    public void recover(HttpClientErrorException ex, AffiliateClientMapDTO affiliateClientMapDTO) throws CallFailedException{
		log.info("Recovered");
		
		try {
			failedCallRepository.save(createFailedCall(ex.getMessage(), affiliateClientMapDTO));
		} catch (JsonProcessingException e) {
			log.info(e.getMessage());
		}
		
        throw new CallFailedException("Call to external API failed");
    }
	
	private FailedCalls createFailedCall(String ex, AffiliateClientMapDTO affiliateClientMapDTO) throws JsonProcessingException {
		FailedCalls failedCall = new FailedCalls();
		
		failedCall.setClientId(affiliateClientMapDTO.getClientId());
		failedCall.setPayload(mapper.writeValueAsString(affiliateClientMapDTO));
		failedCall.setProcessed(false);
		failedCall.setReasonOfFailure(ex);
		failedCall.setRequestType("createClick");
		
		return failedCall;
	}

	@Override
	public JsonNode callSuccessService(AffiliateClientMapDTO affiliateClientMapDTO) throws IOException {
		AffiliateClientMap affiliateClientMap = new AffiliateClientMap();
		JsonNode clickId;
		
		try {
			clickId = extractUUIDFromFile();
		} catch (IOException e) {
			log.info("IOException caught "+ e.getMessage());
			throw new IOException(e.getMessage());
		}
		affiliateClientMap = sourceToDestinationMapper.sourceToDestination(affiliateClientMapDTO);
		affiliateClientMap.setClickId(clickId.get("id").textValue());
		affiliateClientMap = affiliateClientMapRepository.save(affiliateClientMap);
		return clickId;
	}
	
	private JsonNode extractUUIDFromFile() throws IOException {
		Resource resource = resourceLoader.getResource("classpath:FirstExternalApiResponse.txt");
		String myString = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
		JsonNode jsonNode = mapper.readTree(myString);
		return jsonNode;
	}
	

}
