package com.tradingpit.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tradingpit.dto.AffiliateClientMapDTO;
import com.tradingpit.exception.CallFailedException;

@SpringBootTest
public class AffiliateClientMapServiceTest {
	
	@Autowired
	private AffiliateClientMapService service;
	
	@Autowired
	private CallExternalAPIService externalService;
	
	@Test
	void testServiceFailure() throws IOException {
		assertThrows(CallFailedException.class, () -> service.callClicks(makeDto(), false));
	}
	
	@Test
	void testServiceSuccess() throws IOException {
		assertNotNull(service.callClicks(makeDto(), true));
	}
	
	@Test
	void testExternalServiceSuccess() throws IOException {
		assertNotNull(externalService.callSuccessService(makeDto()));
	}
	
	@Test
	void testExternalServiceFailure() {
		assertThrows(CallFailedException.class, () -> externalService.callFailService(makeDto()));
	}
	
	private AffiliateClientMapDTO makeDto() {
		AffiliateClientMapDTO dto = new AffiliateClientMapDTO();
		
		dto.setClientId("0d76e348-9724-11ed-a8fc-0242ac120002");
		dto.setLandingPage("http://exercise/tap/clicks");
		dto.setReferralCode("133114");
		dto.setUserAgent("agent");
		dto.setIp("192.158.1.38");
		
		return dto;
	}
	
}
