package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.request.MemberTierRequest;
import com.wetube.wetube_service.dto.response.MemberTierResponseDto;
import com.wetube.wetube_service.entity.channel.MembershipTier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface  MemberTierMapper {
    MemberTierResponseDto toDto(MembershipTier entity);

    @Mapping(target = "id", ignore = true)
    MembershipTier toEntity(MemberTierResponseDto dto);

    // Request -> DTO
    @Mapping(target = "defaultTier", ignore = true) // Always false
    MemberTierResponseDto requestToDto(MemberTierRequest request);
}
