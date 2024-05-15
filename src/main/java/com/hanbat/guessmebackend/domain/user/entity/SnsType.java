package com.hanbat.guessmebackend.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SnsType {
	KAKAO("카카오"),
	NAVER("네이버"),
	GOOGLE("구글");

	private final String value;
}
