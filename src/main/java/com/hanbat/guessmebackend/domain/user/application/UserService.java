package com.hanbat.guessmebackend.domain.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.login.kakao.dto.UserInfoAndTokenResponse;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoRequest;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoResponse;
import com.hanbat.guessmebackend.domain.user.entity.SnsType;
import com.hanbat.guessmebackend.domain.user.entity.User;
import com.hanbat.guessmebackend.domain.user.repository.UserRepository;
import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;
import com.hanbat.guessmebackend.global.jwt.MemberUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
	private final UserRepository userRepository;
	private final MemberUtil memberUtil;


	@Transactional
	public UserCommonInfoResponse postUserInfo(UserCommonInfoRequest userCommonInfoRequest) {
		final User user = memberUtil.getCurrentUser();
		user.updateUserInfo(userCommonInfoRequest.getRole(), userCommonInfoRequest.getNickname(),
			userCommonInfoRequest.getGender(), userCommonInfoRequest.getBirth());
		userRepository.save(user);
		return UserCommonInfoResponse.fromUser(user);

	}
}
