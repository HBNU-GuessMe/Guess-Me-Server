package com.hanbat.guessmebackend.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
	MAN("남"),
	WOMAN("여"),
	ELSE("기타");

	private final String value;
}
