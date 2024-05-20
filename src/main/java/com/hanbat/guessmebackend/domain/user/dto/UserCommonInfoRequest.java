package com.hanbat.guessmebackend.domain.user.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanbat.guessmebackend.domain.user.entity.Gender;
import com.hanbat.guessmebackend.domain.user.entity.Role;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class UserCommonInfoRequest {
	@NotNull private Role role;
	@NotNull private String nickname;
	@NotNull private Gender gender;

	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@NotNull private LocalDate birth;
}
