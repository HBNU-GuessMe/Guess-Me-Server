package com.hanbat.guessmebackend.domain.login.kakao.dto;

import com.hanbat.guessmebackend.domain.user.entity.SnsType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoAndTokenResponse {
	private Long userId;
	private String email;
	private SnsType snsType;
	private String accessToken;

	@Builder
	public UserInfoAndTokenResponse(Long userId, String email, SnsType snsType, String accessToken) {
		this.userId = userId;
		this.email = email;
		this.snsType = snsType;
		this.accessToken = accessToken;
	}

}
