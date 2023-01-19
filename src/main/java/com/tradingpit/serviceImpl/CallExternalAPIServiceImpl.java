package com.tradingpit.serviceImpl;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingpit.dto.AffiliateClientMapDTO;
import com.tradingpit.dto.FirstExternalApiDTO;
import com.tradingpit.exception.CallFailedException;
import com.tradingpit.mapper.AffiliateResourceDestinationMapper;
import com.tradingpit.model.AffiliateClientMap;
import com.tradingpit.repository.AffiliateClientMapRepository;
import com.tradingpit.repository.FailedCallsRepository;
import com.tradingpit.service.CallExternalAPIService;
import com.tradingpit.util.TradingPitUtil;

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
	private TradingPitUtil util;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Value("${file.firstFile}")
	private String filePath;
	
	private final AffiliateResourceDestinationMapper affiliateToDestinationMapper;
	
	@Override
	public void callFailService(AffiliateClientMapDTO affiliateClientMapDTO) throws ResourceAccessException{
		log.info("Retrying");
		RestTemplate restTemplate = new RestTemplate();
		FirstExternalApiDTO firstExternalApiDTO = affiliateToDestinationMapper.clientDTOToExternalDTO(affiliateClientMapDTO);
	    String result = restTemplate.postForObject(firstExternalApiDTO.getLandingPage(), firstExternalApiDTO, String.class);
	}
	
	@Recover
    public void recover(ResourceAccessException ex, AffiliateClientMapDTO affiliateClientMapDTO) throws CallFailedException{
		log.info("Recovered");
		
		try {
			failedCallRepository.save(util.createFailedCall(ex.getMessage(), mapper.writeValueAsString(affiliateClientMapDTO), affiliateClientMapDTO.getClientId(), "createClick"));
		} catch (JsonProcessingException e) {
			log.info(e.getMessage());
		}
		
		throw new CallFailedException("Call to external API failed");
    }
	
	@Recover
    public void recover(HttpClientErrorException ex, AffiliateClientMapDTO affiliateClientMapDTO) throws CallFailedException{
		log.info("Recovered");
		
		try {
			failedCallRepository.save(util.createFailedCall(ex.getMessage(), mapper.writeValueAsString(affiliateClientMapDTO), affiliateClientMapDTO.getClientId(), "createClick"));
		} catch (JsonProcessingException e) {
			log.info(e.getMessage());
		}
		
        throw new CallFailedException("Call to external API failed");
    }

	@Override
	public JsonNode callSuccessService(AffiliateClientMapDTO affiliateClientMapDTO) throws IOException {
		AffiliateClientMap affiliateClientMap = new AffiliateClientMap();
		JsonNode clickId;
		
		try {
			clickId = util.extractStringFromFile(filePath);
		} catch (IOException e) {
			log.info("IOException caught "+ e.getMessage());
			throw new IOException(e.getMessage());
		}
		affiliateClientMap = affiliateToDestinationMapper.sourceToDestination(affiliateClientMapDTO);
		affiliateClientMap.setClickId(clickId.get("id").textValue());
		affiliateClientMap.setCreationDate(LocalDateTime.now());
		
		affiliateClientMap = affiliateClientMapRepository.save(affiliateClientMap);
		return clickId;
	}

}
