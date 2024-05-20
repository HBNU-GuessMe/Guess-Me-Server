package com.hanbat.guessmebackend.domain.user.dto;

import java.time.LocalDate;

import com.hanbat.guessmebackend.domain.user.entity.Gender;
import com.hanbat.guessmebackend.domain.user.entity.Role;
import com.hanbat.guessmebackend.domain.user.entity.User;


public record UserCommonInfoResponse(Long userId, Role role, String nickname, Gender gender, LocalDate birth) {

	public static UserCommonInfoResponse fromUser(User user) {
		return new UserCommonInfoResponse(
			user.getId(), user.getRole(), user.getNickname(), user.getGender(), user.getBirth()
		);
	}
}
