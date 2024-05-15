package com.hanbat.guessmebackend.domain.login.kakao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanbat.guessmebackend.domain.login.kakao.application.KakaoLoginService;
import com.hanbat.guessmebackend.domain.login.kakao.dto.KakaoTokenResponse;
import com.hanbat.guessmebackend.domain.login.kakao.dto.OauthUserInfoResponse;
import com.hanbat.guessmebackend.domain.login.kakao.dto.UserInfoAndTokenResponse;
import com.hanbat.guessmebackend.global.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth/kakao")
@Slf4j
public class KakaoLoginController {

	private final KakaoLoginService kakaoLoginService;

	private final JwtUtil jwtUtil;

	@GetMapping("/login")
	public ResponseEntity<UserInfoAndTokenResponse> getAccessToken(@RequestParam String code) throws Exception {
		log.info("Kakao code: {}", code);
		// 카카오 엑세스발급 받기
		KakaoTokenResponse kakaoTokenResponse = kakaoLoginService.getAccessToken(code);
		log.info("Kakao token: {}", kakaoTokenResponse);

		if (kakaoTokenResponse == null) {
			throw new Exception("카카오 토큰이 없습니다.");
		}
		// 카카오 유저 정보 얻기
		OauthUserInfoResponse oauthUserInfoResponse = kakaoLoginService.getUserInfo(kakaoTokenResponse.getAccessToken());

		if (oauthUserInfoResponse == null) {
			throw new Exception("카카오 유저 정보를 가져오지못했습니다.");
		}

		// 카카오 유저ID를 이용해서 JWT 엑세스토큰 발급
		String accessToken = jwtUtil.generateToken(oauthUserInfoResponse.getId());

		// 카카오 유저 회원가입
		UserInfoAndTokenResponse userInfoAndTokenResponse = kakaoLoginService.signup(accessToken, oauthUserInfoResponse.getEmail());

		return ResponseEntity.ok(userInfoAndTokenResponse);
	}






}
