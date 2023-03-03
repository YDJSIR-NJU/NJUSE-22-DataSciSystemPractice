package org.example.gateway.service.validation;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.example.common.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class TokenValidation {

    public static final String TOKEN_HEADER = "Authorization";

    public boolean validate(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(TOKEN_HEADER);
        if (token != null) {
            Claims claimsFromToken = JwtUtil.getClaimsFromToken(token);
            if (claimsFromToken == null) {
                log.info("Token: {} is invalid on URL: {}", token, request.getRequestURL());
                return false;
            }

            log.info("Token: {} is valid on URL: {}", token, request.getRequestURL());
            return true;
        }
        return false;
    }
}
