package com.hanbat.guessmebackend.domain.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.family.repository.FamilyRepository;
import com.hanbat.guessmebackend.domain.user.dto.CodeInputResponse;
import com.hanbat.guessmebackend.domain.user.dto.CodeResponse;
import com.hanbat.guessmebackend.domain.user.dto.InterestAndWorriesInfoRequest;
import com.hanbat.guessmebackend.domain.user.dto.InterestAndWorriesInfoResponse;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoRequest;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoResponse;
import com.hanbat.guessmebackend.domain.user.dto.UserTotalResponse;
import com.hanbat.guessmebackend.domain.user.entity.Role;
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
	private final FamilyRepository familyRepository;
	private final MemberUtil memberUtil;

	@Transactional
	public UserCommonInfoResponse postUserInfo(UserCommonInfoRequest userCommonInfoRequest) {
		final User user = memberUtil.getCurrentUser();
		user.updateUserInfo(userCommonInfoRequest.getRole(), userCommonInfoRequest.getNickname(),
			userCommonInfoRequest.getGender(), userCommonInfoRequest.getBirth());
		userRepository.save(user);
		return UserCommonInfoResponse.fromUser(user);

	}

	// 사전정보 -> 피보호자만에서 가족구성원전체로 수정
	@Transactional
	public InterestAndWorriesInfoResponse postInterestAndWorriesUserInfo(InterestAndWorriesInfoRequest interestAndWorriesInfoRequest) {
		final User user = memberUtil.getCurrentUser();
		user.updateWardUserInfo(interestAndWorriesInfoRequest.getInterest(), interestAndWorriesInfoRequest.getWorry());
		userRepository.save(user);
		return InterestAndWorriesInfoResponse.fromUser(user);


	}

	// 코드 입력시 코드가 유저테이블에 있는지 확인 + 이때 코드 소유자가 자기가 소유한 코드를 입력해서 연결시키려고 할 때 예외처리
	public CodeInputResponse validateCode(String code) {
		final User inputUser = memberUtil.getCurrentUser();

		if (inputUser.getUserCode().equals(code)) {
			throw new CustomException(ErrorCode.THIS_CODE_IS_YOUR_CODE);
		}

		User ownerUser = userRepository.findByUserCode(code)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		return CodeInputResponse.builder()
			.ownerId(ownerUser.getId())
			.code(code)
			.isCodeConnected(false)
			.inputUserId(inputUser.getId())
			.inputUserName(inputUser.getNickname())
			.build();
	}

	public CodeResponse getCode() {
		final User user = memberUtil.getCurrentUser();
		return new CodeResponse(user.getUserCode());
	}

	public UserTotalResponse getUserTotalInfo() {
		final User user = memberUtil.getCurrentUser();
		return UserTotalResponse.fromUser(user);
	}


}
