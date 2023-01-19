package com.tradingpit.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tradingpit.dto.AffiliateTransactionsDTO;
import com.tradingpit.dto.SecondExternalApiDTO;
import com.tradingpit.exception.CallFailedException;

@SpringBootTest
public class AffiliateTransactionsServiceTest {
	
	@Autowired
	private AffiliateTransactionsService service;
	
	@Autowired
	private CallSecondExternalAPIService externalService;
	
	
	@Test
	void testServiceSuccess() {
		assertThrows(CallFailedException.class, () -> service.callConversion(makeDto(), false));
	}

	@Test
	void testServiceFailure() throws IOException {
		assertNotNull(service.callConversion(makeDto(), true));
	}
	
	@Test
	void testExternalServiceSuccess() throws IOException {
		assertNotNull(externalService.callSuccessService(makeDto()));
	}
	
	@Test
	void testExternalServiceFailure() {
		assertThrows(CallFailedException.class, () -> externalService.callFailService(makeDto(), makeExternalDto()));
	}
	
	private AffiliateTransactionsDTO makeDto() {
		AffiliateTransactionsDTO dto = new AffiliateTransactionsDTO();
		
		dto.setClientId("0d76e348-9724-11ed-a8fc-0242ac120002");
		dto.setOrderId("0d76e348-9724-11ed-a8fc-0242ac120005");
		dto.setTotalPrice(BigDecimal.valueOf(500.55));
		dto.setTransactionType("New");
		
		return dto;
	}
	
	private SecondExternalApiDTO makeExternalDto() {
		SecondExternalApiDTO dto = new SecondExternalApiDTO();
		
		dto.setAmount(BigDecimal.valueOf(500));
		dto.setClickId("0d76e348-9724-11ed-a8fc-0242ac120005");
		dto.setCurrency("EUR");
		dto.setCustomerId("0d76e348-9724-11ed-a8fc-0242ac120004");
		dto.setExternalId("0d76e348-9724-11ed-a8fc-0242ac120003");
		dto.setIp("192.158.1.38");
		dto.setUserAgent("agent");
		
		return dto;
	}

}
