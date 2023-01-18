package com.tradingpit.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tradingpit.model.AffiliateTransactions;

@Repository
public interface AffiliateTransactionsRepository extends JpaRepository<AffiliateTransactions, BigInteger>{

	
	
}
