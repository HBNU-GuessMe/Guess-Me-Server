package com.hanbat.guessmebackend.domain.login.kakao.application;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.guessmebackend.domain.login.kakao.dto.KakaoTokenResponse;
import com.hanbat.guessmebackend.domain.login.kakao.dto.OauthUserInfoResponse;
import com.hanbat.guessmebackend.domain.login.kakao.dto.UserInfoAndTokenResponse;
import com.hanbat.guessmebackend.domain.user.entity.SnsType;
import com.hanbat.guessmebackend.domain.user.entity.User;
import com.hanbat.guessmebackend.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoLoginService {
	private final UserRepository userRepository;

	private final String reqURL = "https://kauth.kakao.com/oauth/token";
	private final String userInfoURL = "https://kapi.kakao.com/v2/user/me";

	@Value("${oauth2.provider.kakao.grant-type}")
	private String grantType;

	@Value("${oauth2.provider.kakao.client-id}")
	private String clientId;

	@Value("${oauth2.provider.kakao.redirect-url}")
	private String redirectUrl;

	/**
	 * 인가코드를 카카오에 요청해 엑세스 토큰을 받는 함수
	 */
	public KakaoTokenResponse getAccessToken(String code) throws IOException {
		// 파라미터 세팅
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("client_id", clientId);
		params.add("grant_type", grantType);
		params.add("code", code);
		params.add("redirect_uri", redirectUrl);
		params.add("client_secret", clientId);

		// AccessToken POST 요청
		WebClient webClient = WebClient.create(reqURL);
		String response = webClient.post()
			.uri(reqURL)
			.body(BodyInserters.fromFormData(params))
			.header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
			.retrieve()
			.bodyToMono(String.class)
			.block();

		log.info(response);

		// json 형태로 변환
		ObjectMapper mapper = new ObjectMapper();
		KakaoTokenResponse kakaoTokenResponse = null;

		try {

			kakaoTokenResponse = mapper.readValue(response, KakaoTokenResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		return kakaoTokenResponse;

	}

	/**
	 *  카카오 사용자 정보 가져오기
	 */
	public OauthUserInfoResponse getUserInfo(String token) throws JsonProcessingException {

		// 유저정보 GET 요청
		WebClient webClient = WebClient.create(userInfoURL);
		String response = webClient.get()
			.uri(userInfoURL)
			.header("Authorization", "Bearer "+ token)
			.retrieve()
			.bodyToMono(String.class)
			.block();

		log.info(response);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response);
		log.info(jsonNode.asText());
		return new OauthUserInfoResponse(jsonNode.get("id").asLong(), jsonNode.get("kakao_account").get("email").asText());
	}

	/*
		로그인한 사용자 회원가입
	 */
	@Transactional
	public UserInfoAndTokenResponse signup(String accessToken, String email) {
		User user = userRepository.save(new User(email, SnsType.KAKAO));
		return UserInfoAndTokenResponse.builder()
			.userId(user.getId())
			.email(email)
			.snsType(user.getSnsType())
			.accessToken(accessToken)
			.build();

	}
}
