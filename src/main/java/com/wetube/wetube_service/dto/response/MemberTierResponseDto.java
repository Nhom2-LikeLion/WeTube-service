package com.wetube.wetube_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberTierResponseDto {
    private UUID id;
    private String title;
    private float price;
    private boolean isDefault;
    private String description;
}
