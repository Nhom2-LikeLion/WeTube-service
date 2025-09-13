package com.wetube.wetube_service.dto.response;

import java.util.List;

public record MeResponseDto(String sub, String email, String picture, String name, List<String> roles) {
}
