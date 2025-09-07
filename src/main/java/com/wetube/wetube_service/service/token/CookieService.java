package com.wetube.wetube_service.service.token;

import jakarta.servlet.http.HttpServletResponse;

public interface CookieService {
    void setAccessCookie(HttpServletResponse res, String token);
    void setRefreshCookie(HttpServletResponse res, String token);
}
