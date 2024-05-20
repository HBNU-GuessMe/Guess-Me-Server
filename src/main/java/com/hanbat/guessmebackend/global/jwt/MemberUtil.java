package com.hanbat.guessmebackend.global.jwt;

import org.springframework.stereotype.Component;

import com.hanbat.guessmebackend.domain.user.entity.User;
import com.hanbat.guessmebackend.domain.user.repository.UserRepository;
import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberUtil {
	private final SecurityUtil securityUtil;
	private final UserRepository userRepository;

	public User getCurrentUser() {
		return userRepository
			.findById(securityUtil.getCurrentMemberId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	public User getUserByUserId(Long userId) {
		return userRepository
			.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}
}
