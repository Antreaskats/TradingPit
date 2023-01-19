package com.tradingpit.controller;

import java.io.IOException;

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

@RestController
@RequestMapping("/register")
public class AffiliateClientController {
	
	@Autowired
	private AffiliateClientMapService clientService;
	
	@Autowired
	private AffiliateTransactionsService transactionService;

	@PostMapping("/client")
	public JsonNode clientCall(@Valid @RequestBody AffiliateClientMapDTO affiliateClientMapDTO, @RequestHeader("successful") boolean successful){
		JsonNode jsonResponse = null;
		try {
			jsonResponse = clientService.callClicks(affiliateClientMapDTO, successful);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResponse;
		
	}
	
	@PostMapping("/conversion")
	public ResponseEntity<JsonNode> conversionCall(@Valid @RequestBody AffiliateTransactionsDTO affiliateTransactionsDTO, @RequestHeader("successful") boolean successful) throws IOException{
		JsonNode jsonResponse = null;
		
		jsonResponse = transactionService.callConversion(affiliateTransactionsDTO, successful);
		
		
		return new ResponseEntity<JsonNode>(jsonResponse, HttpStatus.CREATED);
		
	}
	
	
	
}
