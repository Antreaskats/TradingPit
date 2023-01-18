package com.tradingpit.mapper;

import org.mapstruct.Mapper;

import com.tradingpit.dto.AffiliateClientMapDTO;
import com.tradingpit.model.AffiliateClientMap;

@Mapper(componentModel = "spring")
public interface SimpleResourceDestinationMapper {
	
	AffiliateClientMap sourceToDestination(AffiliateClientMapDTO source);
	AffiliateClientMapDTO destinationToSource(AffiliateClientMap destination);

}
