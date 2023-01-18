package com.tradingpit.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tradingpit.model.AffiliateTransactions;
import com.tradingpit.repository.AffiliateTransactionsRepository;
import com.tradingpit.service.AffiliateTransactionsService;

@Service
public class AffiliateTransactionsServiceImpl implements AffiliateTransactionsService{
	
	@Autowired
	private AffiliateTransactionsRepository repository;

	@Override
	public AffiliateTransactions callConversion(AffiliateTransactions transaction) {
		return repository.save(transaction);
	}

}
