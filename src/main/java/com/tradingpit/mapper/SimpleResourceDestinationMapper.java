package com.tradingpit.mapper;

import org.mapstruct.Mapper;

import com.tradingpit.dto.AffiliateClientMapDTO;
import com.tradingpit.dto.FirstExternalApiDTO;
import com.tradingpit.model.AffiliateClientMap;

@Mapper(componentModel = "spring")
public interface SimpleResourceDestinationMapper {
	
	AffiliateClientMap sourceToDestination(AffiliateClientMapDTO source);
	AffiliateClientMapDTO destinationToSource(AffiliateClientMap destination);
	
	FirstExternalApiDTO clientDTOToExternalDTO(AffiliateClientMapDTO source);
	AffiliateClientMapDTO externalDTOToClientDTO(FirstExternalApiDTO destination);

}
