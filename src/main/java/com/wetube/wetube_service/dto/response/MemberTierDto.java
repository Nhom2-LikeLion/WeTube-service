package com.wetube.wetube_service.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberTierDto {
    private int id;
    private String title;
    private float price;
    private String description;
    private boolean isDefault;
}
