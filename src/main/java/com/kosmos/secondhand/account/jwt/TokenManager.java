package com.kosmos.secondhand.account.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Setter @Getter
@Slf4j
public class TokenManager {

    private final String AUTHORITY_KEY = "auth";
    private final String secret = "aS1sb3ZlLXByb2dyYW1pbmctYnV0LWktZG9udC1saWtlLXNob3BwaW5naS1sb3ZlLXByb2dyYW1pbmctYnV0LWktZG9udC1saWtlLXNob3BwaW5naS1sb3ZlLXByb2dyYW1pbmctYnV0LWktZG9udC1saWtlLXNob3BwaW5n";
    private final Long tokenLifeTime = 86400000L;
    private Key key;

    public TokenManager() {
        byte[] bytes = Decoders.BASE64.decode(this.secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenLifeTime);
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITY_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}
