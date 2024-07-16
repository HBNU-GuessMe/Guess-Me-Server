package com.hanbat.guessmebackend.domain.user.dto;

import com.hanbat.guessmebackend.domain.user.entity.Role;
import com.hanbat.guessmebackend.domain.user.entity.User;

public record InterestAndWorriesInfoResponse(Long userId, String nickname, Role role, String interest, String worry){

	public static InterestAndWorriesInfoResponse fromUser(User user) {
		return new InterestAndWorriesInfoResponse(
			user.getId(), user.getNickname(), user.getRole(), user.getInterest(), user.getWorry()
		);
	}
}
