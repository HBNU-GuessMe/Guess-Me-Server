package com.hanbat.guessmebackend.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	GUARDIAN("보호자"),
	WARD("피보호자");

	@JsonValue
	private final String value;


}
