package com.wetube.wetube_service.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    @Id
    private UUID id;
    private String name;
    private LocalDate createdAt;
    private Integer count;
}
