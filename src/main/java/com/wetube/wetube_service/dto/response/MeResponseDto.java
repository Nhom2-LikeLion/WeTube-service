package com.wetube.wetube_service.dto.response;

import lombok.Value;

import java.util.List;

@Value
public class MeResponseDto {
    String sub;
    String username;
    String email;
    List<String> roles;
}
