package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.request.MemberTierRequest;
import com.wetube.wetube_service.dto.response.MemberTierDto;
import com.wetube.wetube_service.entity.channel.MembershipTier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface  MemberTierMapper {
    MemberTierDto toDto(MembershipTier entity);

    @Mapping(target = "id", ignore = true)
    MembershipTier toEntity(MemberTierDto dto);

    // Request -> DTO
    MemberTierDto requestToDto(MemberTierRequest request);

}
