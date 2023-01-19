package com.tradingpit.service;


import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.tradingpit.dto.AffiliateClientMapDTO;

public interface AffiliateClientMapService {

	public JsonNode callClicks(AffiliateClientMapDTO affiliateClientMapDTO, boolean successful) throws IOException;
	
}
