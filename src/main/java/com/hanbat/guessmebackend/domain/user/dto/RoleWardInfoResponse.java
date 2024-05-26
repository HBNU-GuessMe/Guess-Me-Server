package com.hanbat.guessmebackend.domain.user.dto;

import com.hanbat.guessmebackend.domain.user.entity.Role;
import com.hanbat.guessmebackend.domain.user.entity.User;

public record RoleWardInfoResponse(Long userId, String nickname, Role role, String interest, String worry){

	public static RoleWardInfoResponse fromUser(User user) {
		return new RoleWardInfoResponse(
			user.getId(), user.getNickname(), user.getRole(), user.getInterest(), user.getWorry()
		);
	}
}
