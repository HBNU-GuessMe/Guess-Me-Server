package com.hanbat.guessmebackend.global.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.hanbat.guessmebackend.global.jwt.JwtFilter;
import com.hanbat.guessmebackend.global.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSecurity httpSecurity, JwtUtil jwtUtil) throws Exception {
		http
			.cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();

					// 프론트엔드에서 데이터를 보낼 포트 허용
					config.setAllowedMethods(Collections.singletonList("*"));
					config.setAllowCredentials(true);
					config.setAllowedHeaders(Collections.singletonList("*"));
					config.setMaxAge(3600L);
					config.setExposedHeaders(Collections.singletonList("Authorization"));
					config.setAllowedOriginPatterns(Collections.singletonList("*"));

					return config;
				}
			})));

		// token 방식은 세션을 STATELESS로 관리
		http
			.csrf((auth) -> auth.disable());

		// form login 방식 사용 x
		http
			.formLogin((auth) -> auth.disable());

		// 로그인한 사용자만 접근 가능
		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/oauth/kakao/login", "/oauth/kakao/token/create", "/home").permitAll()
				.anyRequest().authenticated());

		// 필터 등록
		http
			.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		// JWT 방식에서는 항상 세션을 STATELESS로 설정
		http
			.sessionManagement((sesion) -> sesion
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
