package com.hanbat.guessmebackend.domain.login.kakao.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Deprecated
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthUserInfoResponse {
	private String snsId;

	public OauthUserInfoResponse(String snsId) {
		this.snsId = snsId;

	}
}
