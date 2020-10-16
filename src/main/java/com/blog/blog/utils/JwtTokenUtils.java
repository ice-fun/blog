package com.blog.blog.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
//@ConfigurationProperties(prefix = "jwt.config")
public class JwtTokenUtils implements Serializable {


    private static String secret;

    private static String token;

    private static Long expiration;
    private static Clock clock = DefaultClock.INSTANCE;

    public static String generateToken(Map<String, Object> claims, String subject) {
        final Date createDate = clock.now();
        final Date expirationDate = new Date(createDate.getTime() + expiration);

        return "Bearer " + Jwts.builder().
                setClaims(claims).setSubject(subject)
                .setIssuedAt(createDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static void validateToken(String token, String subject, Integer tokenVersion) throws JwtException {
        Claims claims = getClaimsFromToken(token);
        Integer tTokenVersion = claims.get("tokenVersion", Integer.class);
        boolean flag = true;
        flag = subject.equals(getClaimFromToken(token, Claims::getSubject));
        if (!flag) {
            throw new JwtException("请重新登录");
        }
        flag = isTokenExpired(token);
        if (flag) {
            throw new JwtException("请重新登录");
        }
        flag = tokenVersion.equals(tTokenVersion);
        if (!flag) {
            throw new JwtException("请重新登录");
        }
    }

    public static String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //
    private static <T> T getClaimFromToken(String token, Function<Claims, T> function) {
        final Claims claims = getClaimsFromToken(token);
        return function.apply(claims);
    }

    //
    private static Claims getClaimsFromToken(String token) {
        Claims body = null;
        try {
            body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("请重新登录");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return body;
    }

    private static Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    public static Boolean isTokenAlmostExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.getTime() < (clock.now().getTime() - 1000 * 60 * 60);
    }

    public static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public static void main(String[] args) {
    }

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        JwtTokenUtils.secret = secret;
    }

    @Value("${jwt.expiration}")
    public void setExpiration(long expire) {
        JwtTokenUtils.expiration = expire;
    }

    @Value("${jwt.token}")
    public void setToken(String token) {
        JwtTokenUtils.token = token;
    }
}
