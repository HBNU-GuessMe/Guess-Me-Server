package com.hanbat.guessmebackend.domain.login.kakao.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserInfoRequest {
	@NotNull private String snsId;
}
