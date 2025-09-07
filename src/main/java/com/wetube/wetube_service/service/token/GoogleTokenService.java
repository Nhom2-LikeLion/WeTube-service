package com.wetube.wetube_service.service.token;


import com.wetube.wetube_service.dto.GoogleUser;
import com.wetube.wetube_service.dto.response.GoogleTokenResponse;

public interface GoogleTokenService {
    GoogleTokenResponse exchangeCode(String code);
                                     
    GoogleUser parseAndVerify(String idToken);
}
