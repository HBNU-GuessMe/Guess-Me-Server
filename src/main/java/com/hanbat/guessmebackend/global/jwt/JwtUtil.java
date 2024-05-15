package com.hanbat.guessmebackend.global.jwt;

import java.time.Duration;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	private static final Long accessTokenValidTime = Duration.ofHours(1).toMillis(); // 1시간
	private static final Long refreshTokenValidTime = Duration.ofDays(14).toMillis(); // 14주

	@Value("${jwt.secret}")
	private String secret;

	// 회원 정보 조회
	public Long getUserId(String token){
		return Jwts.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token)
			.getBody()
			.get("userId", Long.class);
	}

	// 토큰 유효 및 만료 확인
	public boolean isExpired(String token){
		Claims claims = Jwts.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token)
			.getBody();

		Date expiration = claims.getExpiration();
		return expiration.before(new Date());
	}

	// Access Token 확인
	public boolean isAccessToken(String token) {
		Header header = Jwts.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token)
			.getHeader();

		return header.get("type").toString().equals("access");
	}

	// Refresh Token 확인
	public boolean isRefreshToken(String token) {
		Header header = Jwts.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token)
			.getHeader();

		return header.get("type").toString().equals("refresh");
	}

	// Access Token 생성
	public String generateToken(Long userId) {
		return createJwt(userId, "access", accessTokenValidTime);
	}

	// Refresh Token 생성
	public String generateRefreshToken(Long userId) {
		return createJwt(userId, "refresh", refreshTokenValidTime);
	}

	// Jwt Token 생성
	public String createJwt(Long userId, String type, Long tokenValidTime) {
		Claims claims = Jwts.claims();
		claims.put("userId", userId);

		return Jwts.builder()
			.setHeaderParam("type", type)
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
			.signWith(SignatureAlgorithm.HS256, secret)
			.compact();
	}

}
