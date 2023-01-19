package com.tradingpit.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradingpit.dto.AffiliateClientMapDTO;
import com.tradingpit.dto.AffiliateTransactionsDTO;
import com.tradingpit.service.AffiliateClientMapService;
import com.tradingpit.service.AffiliateTransactionsService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/register")
@Slf4j
public class AffiliateClientController {
	
	@Autowired
	private AffiliateClientMapService clientService;
	
	@Autowired
	private AffiliateTransactionsService transactionService;

	@PostMapping("/client")
	public ResponseEntity<JsonNode> clientCall(@Valid @RequestBody AffiliateClientMapDTO affiliateClientMapDTO, @RequestHeader("successful") boolean successful) throws IOException{
		JsonNode jsonResponse = null;
		try {
			jsonResponse = clientService.callClicks(affiliateClientMapDTO, successful);
		} catch (IOException e) {
			log.info(e.getMessage());
			throw new IOException(e.getMessage());
		}
		
		return new ResponseEntity<JsonNode>(jsonResponse, HttpStatus.OK);
	
	}
	
	@PostMapping("/conversion")
	public ResponseEntity<?> conversionCall(@Valid @RequestBody AffiliateTransactionsDTO affiliateTransactionsDTO, @RequestHeader("successful") boolean successful) throws IOException{
		JsonNode jsonResponse = null;
		Map<String,String> jsonId = new HashMap<>();
		
		jsonResponse = transactionService.callConversion(affiliateTransactionsDTO, successful);

		jsonId.put("id", jsonResponse.get("id").asText());
	
		return new ResponseEntity<Object>(jsonId, HttpStatus.OK);
	
	}
	
	
	
}
