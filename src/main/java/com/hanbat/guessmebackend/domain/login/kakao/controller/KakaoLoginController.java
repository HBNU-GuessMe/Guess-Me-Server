package com.hanbat.guessmebackend.domain.login.kakao.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanbat.guessmebackend.domain.login.kakao.application.KakaoLoginService;
import com.hanbat.guessmebackend.domain.login.kakao.dto.KakaoToken;
import com.hanbat.guessmebackend.domain.login.kakao.dto.KakaoUserInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/kakao")
@Slf4j
public class KakaoLoginController {

	private final KakaoLoginService kakaoLoginService;

	@GetMapping("/info")
	public ResponseEntity<KakaoUserInfo> getAccessToken(@RequestParam String code) throws IOException {
		log.info("Kakao code: {}", code);
		KakaoToken kakaoToken = kakaoLoginService.getAccessToken(code);
		log.info("Kakao token: {}", kakaoToken);
		if (kakaoToken != null) {
			return ResponseEntity.ok(kakaoLoginService.getUserInfo(kakaoToken.getAccessToken()));
		}
		return ResponseEntity.internalServerError().build();

	}

}
