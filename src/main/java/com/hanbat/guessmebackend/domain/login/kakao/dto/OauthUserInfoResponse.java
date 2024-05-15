package com.hanbat.guessmebackend.domain.login.kakao.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthUserInfoResponse {
	private Long id;
	private String email;

	public OauthUserInfoResponse(Long id, String email) {
		this.id = id;
		this.email = email;

	}
}
