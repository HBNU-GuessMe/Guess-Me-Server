package com.hanbat.guessmebackend.global.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String path = request.getRequestURI();
		log.info(path);

		// 로그인 요청일경우 건너뛰기
		if (path.startsWith("/oauth") || path.endsWith("home") || path.endsWith("/ws/chat")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String authorizationHeader = request.getHeader("Authorization");
		log.info("authorization header: {}", authorizationHeader);

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);

		}

		// 토큰 꺼내기
		String token = authorizationHeader.split(" ")[1];

		// 토큰 만료기간 확인
		if (jwtUtil.isExpired(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 토큰에서 UserId 꺼내기
		Long userId = jwtUtil.getUserId(token);
		log.info("userId: {}", userId);

		// 권한 부여
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null,
			List.of(new SimpleGrantedAuthority("USER")));

		// Detail을 token에 추가
		authenticationToken.setDetails(new WebAuthenticationDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		filterChain.doFilter(request, response);

	}
}
