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
	// 코드가 이미 가족과 연결되어있는 코드인지 여부
	private Boolean isCodeConnected;
	private Long inputUserId;
	private String inputUserName;


	@Builder
	public CodeInputResponse(Long ownerId, String code, Boolean isCodeConnected, Long inputUserId, String inputUserName) {
		this.ownerId = ownerId;
		this.code = code;
		this.isCodeConnected = isCodeConnected;
		this.inputUserId = inputUserId;
		this.inputUserName = inputUserName;
	}
}

