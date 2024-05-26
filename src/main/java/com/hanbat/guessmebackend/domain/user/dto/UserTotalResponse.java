package com.hanbat.guessmebackend.domain.user.dto;

import java.time.LocalDate;

import com.hanbat.guessmebackend.domain.user.entity.Gender;
import com.hanbat.guessmebackend.domain.user.entity.Role;
import com.hanbat.guessmebackend.domain.user.entity.User;

public record UserTotalResponse(Long id, Long familyId, Role role, String nickname, Gender gender,
								LocalDate birth, String interest, String worry) {
	public static UserTotalResponse fromUser(User user) {
		return new UserTotalResponse(
			user.getId(), user.getFamily().getId(), user.getRole(), user.getNickname(), user.getGender(), user.getBirth()
			, user.getInterest(), user.getWorry()
		);
	}
}
