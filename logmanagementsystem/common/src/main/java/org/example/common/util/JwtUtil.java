package org.example.common.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.example.common.vo.UserVo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {

    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    private static final String key = "logManagementSystem";
    //过期时间/ms
    private static final long expiration = 1800000L;
    public static String TOKEN_HEADER = "Authorization";

    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate()).signWith(SignatureAlgorithm.HS512, key).compact();
    }


    public static Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration);
    }

    public static Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.warn("JWT validation failure on token: {}. The exception message is: {}", token, e.getMessage());
        }
        log.info("JWT validation successful on token {}.", token);
        return claims;
    }

    public static int getUserIdFromClaims(Claims claims) {
        return Integer.parseInt((String) claims.get(USER_ID));
    }

    public static String getUserNameFromClaims(Claims claims) {
        return (String) claims.get(USER_NAME);
    }


    public static Date getExpiredDateFromClaims(Claims claims) {
        return claims.getExpiration();
    }

    public static Date getExpiredDateFromToken(String token) {
        return getExpiredDateFromClaims(getClaimsFromToken(token));
    }

    public static boolean isTokenExpired(String token) {
        return getExpiredDateFromToken(token).before(new Date());
    }

    public static String generateTokenByUser(UserVo user) {
        Map<String, Object> map = new HashMap<>();
        map.put(USER_ID, String.valueOf(user.getUserId()));
        map.put(USER_NAME, user.getUserName());
        return generateToken(map);
    }

}
