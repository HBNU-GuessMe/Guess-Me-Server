package com.hanbat.guessmebackend.domain.login.kakao.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoUserInfo {
	private Long id;

	private String email;

	public KakaoUserInfo(Long id, String email) {
		this.id = id;
		this.email = email;
	}
}
