package com.tradingpit.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tradingpit.model.AffiliateClientMap;


@Repository
public interface AffiliateClientMapRepository extends JpaRepository<AffiliateClientMap, BigInteger>{

}
