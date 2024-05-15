package com.hanbat.guessmebackend.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	GUARDIAN("보호자"),
	WARD("피보호자");

	private final String value;
}
