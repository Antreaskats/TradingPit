package com.tradingpit.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public JsonNode clientCall(@Valid @RequestBody AffiliateClientMapDTO affiliateClientMapDTO){
		JsonNode jsonResponse = null;
		try {
			jsonResponse = clientService.callClicks(affiliateClientMapDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResponse;
		
	}
	
	@PostMapping("/conversion")
	public ResponseEntity<JsonNode> conversionCall(@Valid @RequestBody AffiliateTransactionsDTO affiliateTransactionsDTO) throws IOException{
		JsonNode jsonResponse = null;
		
		jsonResponse = transactionService.callConversion(affiliateTransactionsDTO);
		
		
		return new ResponseEntity<JsonNode>(jsonResponse, HttpStatus.CREATED);
		
	}
	
	
	
}
