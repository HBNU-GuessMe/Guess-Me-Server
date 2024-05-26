package com.hanbat.guessmebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeResponse {
	private String code;

	public CodeResponse(String code) {
		this.code = code;
	}
}
