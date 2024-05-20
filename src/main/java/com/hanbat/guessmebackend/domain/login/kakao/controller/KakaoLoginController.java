package com.hanbat.guessmebackend.domain.login.kakao.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanbat.guessmebackend.domain.login.kakao.application.KakaoLoginService;
import com.hanbat.guessmebackend.domain.login.kakao.dto.KakaoTokenResponse;
import com.hanbat.guessmebackend.domain.login.kakao.dto.OauthUserInfoResponse;
import com.hanbat.guessmebackend.domain.login.kakao.dto.UserInfoAndTokenResponse;
import com.hanbat.guessmebackend.domain.login.kakao.dto.UserInfoRequest;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/kakao")
@Slf4j
public class KakaoLoginController {

	private final KakaoLoginService kakaoLoginService;


	@Deprecated
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
		OauthUserInfoResponse oauthUserInfoResponse = kakaoLoginService.getUserInfo(
			kakaoTokenResponse.getAccessToken());

		if (oauthUserInfoResponse == null) {
			throw new Exception("카카오 유저 정보를 가져오지못했습니다.");
		}

		// 카카오 유저 회원가입
		UserInfoAndTokenResponse userInfoAndTokenResponse = kakaoLoginService.signup(oauthUserInfoResponse.getSnsId());

		return ResponseEntity.ok(userInfoAndTokenResponse);
	}
	/*
	 카카오 로그인 시 JWT 토큰 생성
	 카카오 토큰이 만료되어 다시 로그인할 경우에도 다시 JWT 토큰 생성
	 */

	@PostMapping("/token/create")
	public ResponseEntity<UserInfoAndTokenResponse> createJwtToken(@Valid @RequestBody UserInfoRequest userInfoReqeust) {
		log.info(userInfoReqeust.getSnsId().toString());
		// 카카오 유저 회원가입
		UserInfoAndTokenResponse userInfoAndTokenResponse = kakaoLoginService.signup(userInfoReqeust.getSnsId());

		return ResponseEntity.status(HttpStatus.OK).body(userInfoAndTokenResponse);
	}




}






