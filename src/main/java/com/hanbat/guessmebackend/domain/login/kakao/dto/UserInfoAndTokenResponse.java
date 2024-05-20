package com.hanbat.guessmebackend.domain.login.kakao.dto;

import com.hanbat.guessmebackend.domain.user.entity.SnsType;
import com.hanbat.guessmebackend.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public record UserInfoAndTokenResponse (Long userId, String snsId, SnsType snsType, String accessToken) {

	public static UserInfoAndTokenResponse fromUser(User user, String accessToken) {
		return new UserInfoAndTokenResponse(user.getId(), user.getSnsId(), user.getSnsType(), accessToken);
	}

}
