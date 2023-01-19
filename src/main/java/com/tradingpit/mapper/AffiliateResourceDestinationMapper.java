package com.tradingpit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.tradingpit.dto.AffiliateClientMapDTO;
import com.tradingpit.dto.AffiliateTransactionsDTO;
import com.tradingpit.dto.FirstExternalApiDTO;
import com.tradingpit.dto.SecondExternalApiDTO;
import com.tradingpit.model.AffiliateClientMap;
import com.tradingpit.model.AffiliateTransactions;

@Mapper(componentModel = "spring")
public interface AffiliateResourceDestinationMapper {
	
	AffiliateClientMap sourceToDestination(AffiliateClientMapDTO source);
	AffiliateClientMapDTO destinationToSource(AffiliateClientMap destination);
	
	FirstExternalApiDTO clientDTOToExternalDTO(AffiliateClientMapDTO source);
	AffiliateClientMapDTO externalDTOToClientDTO(FirstExternalApiDTO destination);

	SecondExternalApiDTO clientMapToSecondExternalDTO(AffiliateClientMap source);
	AffiliateClientMap SecondExternalDTOToClientMap(SecondExternalApiDTO destination);
	
	@Mapping(target = "externalId", source = "source.orderId")
	@Mapping(target = "customerId", source = "source.clientId")
	@Mapping(target = "amount", source = "source.totalPrice")
	SecondExternalApiDTO updateSecondExternalDTO(@MappingTarget SecondExternalApiDTO secondExternalApiDTO, AffiliateTransactionsDTO source);
	
	@Mapping(target = "orderId", source = "destination.externalId")
	@Mapping(target = "clientId", source = "destination.customerId")
	@Mapping(target = "totalPrice", source = "destination.amount")
	AffiliateTransactionsDTO SecondExternalDTOToTransactionsDTO(SecondExternalApiDTO destination);
	
	@Mapping(target = "orderAmount", source = "source.totalPrice")
	AffiliateTransactions transactionsDTOToTransactions(AffiliateTransactionsDTO source);
	
	@Mapping(target = "totalPrice", source = "destination.orderAmount")
	AffiliateTransactionsDTO transactionsToTransactionsDTO(AffiliateTransactions destination);

}
