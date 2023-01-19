package com.tradingpit.service;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradingpit.dto.AffiliateTransactionsDTO;

public interface AffiliateTransactionsService {
	
	public JsonNode callConversion(AffiliateTransactionsDTO affiliateTransactionsDTO, boolean successful) throws IOException;

}
