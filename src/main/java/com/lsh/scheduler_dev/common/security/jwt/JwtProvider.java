package com.lsh.scheduler_dev.common.security.jwt;

import com.lsh.scheduler_dev.common.security.jwt.provieder.constants.TokenExpiredConstant;
import com.lsh.scheduler_dev.common.security.jwt.provieder.dto.MemberAuthDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {
    private final SecretKey accessSecretKey;
    private final TokenExpiredConstant tokenExpiredConstant;

    public JwtProvider(@Value("${spring.jwt.secret}") String accessSecretKey,
                       TokenExpiredConstant tokenExpiredConstant) {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes());
        this.tokenExpiredConstant = tokenExpiredConstant;
    }

    public String generateToken(MemberAuthDto memberAuthDto, Date date) {
        return Jwts.builder()
                .subject(memberAuthDto.getEmail())
                .id(memberAuthDto.getMemberId().toString())
                .claim("type", "AT")
                .issuedAt(date)
                .expiration(tokenExpiredConstant.getAccessTokenExpiredDate(date))
                .signWith(accessSecretKey, Jwts.SIG.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        MemberAuthDto memberAuthDto = getMemberAuthDto(token);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(memberAuthDto, token, authorities);
    }

    public boolean isTokenExpired(String token) {
        Claims claims = parsClaims(token);
        return claims.getExpiration().before(new Date());
    }

    public long getExpiration(String token) {
        Claims claims = parsClaims(token);
        return claims.getExpiration().getTime();
    }

    private MemberAuthDto getMemberAuthDto(String token) {
        Claims claims = parsClaims(token);
        return MemberAuthDto.builder()
                .memberId(Long.valueOf(claims.getId()))
                .email(claims.getSubject())
                .build();
    }

    private Claims parsClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.accessSecretKey).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
