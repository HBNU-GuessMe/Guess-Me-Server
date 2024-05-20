package com.hanbat.guessmebackend.domain.login.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Deprecated
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoTokenResponse {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("expires_in")
	private int expiresIn;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("refresh_token_expires_in")
	private int refreshTokenExpiresIn;

	private String scope;


	@Builder
	public KakaoTokenResponse(String accessToken, String refreshToken, int expiresIn, String tokenType, String scope) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expiresIn = expiresIn;
		this.tokenType = tokenType;
		this.refreshTokenExpiresIn = expiresIn;
		this.scope = scope;
	}
}
