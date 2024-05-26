package com.hanbat.guessmebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeInputResponse {
	private Long ownerId;
	private String code;
	private Long inputUserId;

	@Builder
	public CodeInputResponse(Long ownerId, String code, Long inputUserId) {
		this.ownerId = ownerId;
		this.code = code;
		this.inputUserId = inputUserId;
	}
}

