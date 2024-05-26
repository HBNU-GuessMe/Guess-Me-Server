package com.hanbat.guessmebackend.domain.user.application;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.user.dto.CodeInputResponse;
import com.hanbat.guessmebackend.domain.user.dto.CodeResponse;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoRequest;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoResponse;
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

	public CodeInputResponse validateCode(String code) {
		final User inputUser = memberUtil.getCurrentUser();
		User ownerUser = userRepository.findByUserCode(code)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


		Optional.of(code)
			.filter(c -> c.equals(ownerUser.getUserCode()))
			.ifPresentOrElse(
				c -> {},
				() -> { throw new CustomException(ErrorCode.USER_CODE_NOT_MATCHED); });

		return CodeInputResponse.builder()
			.ownerId(ownerUser.getId())
			.code(code)
			.inputUserId(inputUser.getId())
			.build();
	}

	public CodeResponse getCode() {
		final User user = memberUtil.getCurrentUser();
		return new CodeResponse(user.getUserCode());
	}
}
