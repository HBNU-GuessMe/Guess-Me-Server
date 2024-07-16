package com.hanbat.guessmebackend.domain.family.application;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.family.dto.FamilyConnectionRequest;
import com.hanbat.guessmebackend.domain.family.dto.FamilyConnectionWithOwnerRequest;
import com.hanbat.guessmebackend.domain.family.dto.FamilyUserInfoResponse;
import com.hanbat.guessmebackend.domain.family.entity.Family;
import com.hanbat.guessmebackend.domain.family.repository.FamilyRepository;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoResponse;
import com.hanbat.guessmebackend.domain.user.entity.User;
import com.hanbat.guessmebackend.domain.user.repository.UserRepository;
import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;
import com.hanbat.guessmebackend.global.jwt.MemberUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FamilyService {
	private final FamilyRepository familyRepository;
	private final UserRepository userRepository;
	private final MemberUtil memberUtil;

	/*
		가족 코드 owner가 가족 연결
	 */
	@Transactional
	public FamilyUserInfoResponse connectFamilyWithOwner(
		FamilyConnectionWithOwnerRequest familyConnectionWithOwnerRequest) {
		final User ownerUser = memberUtil.getCurrentUser();

		// request로 받은 코드와 연결하기를 누른 사용자의 코드가 일치하는 확인 (코드 사용자만 연결하기 누르기 가능)
		Optional.of(familyConnectionWithOwnerRequest.getCode())
			.filter((c) -> c.equals(ownerUser.getUserCode()))
			.ifPresentOrElse(
				c -> {},
				() -> { throw new CustomException(ErrorCode.FAMILY_CODE_IS_NOT_OWNER);});


		// owner가 연결을 하려는데, 이미 가족이 존재한다면, 에러 발생
		if (familyRepository.existsFamilyByFamilyCode(familyConnectionWithOwnerRequest.getCode())) {
			throw new CustomException(ErrorCode.FAMILY_ALREADY_REGISTERED);
		}

		// 가족이 존재하지않는다면, 가족 저장 후 해당 유저들의 family 업데이트
		// 연결시킨 가족 구성원 수 + 가족코드 소유자 1명
		int count = familyConnectionWithOwnerRequest.getUserIds().size() + 1;

		Family family = Family.builder()
			.familyCode(familyConnectionWithOwnerRequest.getCode())
			.count(count)
			.build();

		familyRepository.save(family);

		familyConnectionWithOwnerRequest.getUserIds().forEach(userId -> {
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
			user.updateFamily(family);
		});

		ownerUser.updateFamily(family);

		List<User> users = familyRepository.findUsersByFamilyId(family.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.FAMILY_AND_USERS_NOT_FOUND));

		List<UserCommonInfoResponse> userResponses = users.stream()
			.map(UserCommonInfoResponse::fromUser)
			.toList();


		return FamilyUserInfoResponse.fromFamily(family, userResponses);

	}

	/*
		다른 가족 구성원이 가족에 추가로 조인
		가족이 존재한다면, 가족을 찾아서 count 업데이트하고, 해당 유저의 family 업데이트
	 */
	@Transactional
	public FamilyUserInfoResponse connectFamily(FamilyConnectionRequest familyConnectionRequest) {
		final User user = memberUtil.getCurrentUser();

		Family family = familyRepository.findFamilyByFamilyCode(familyConnectionRequest.getCode())
			.orElseThrow(() -> new CustomException(ErrorCode.FAMILY_NOT_FOUND));

		// 이미 존재하는 가족의 구성원 수 + 새로 들어운 가족 구성원 수
		int count = family.getCount() + 1;

		family.updateCount(count);

		user.updateFamily(family);

		List<User> users = familyRepository.findUsersByFamilyId(family.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.FAMILY_AND_USERS_NOT_FOUND));

		List<UserCommonInfoResponse> userResponses = users.stream()
			.map(UserCommonInfoResponse::fromUser)
			.toList();


		return FamilyUserInfoResponse.fromFamily(family, userResponses);
	}

	/*
		가족 조회
	 */

	public FamilyUserInfoResponse getFamilyInfo(Long familyId) {

		Family family = familyRepository.findById(familyId)
			.orElseThrow(() -> new CustomException(ErrorCode.FAMILY_NOT_FOUND));

		List<User> users = familyRepository.findUsersByFamilyId(familyId)
			.orElseThrow(() -> new CustomException(ErrorCode.FAMILY_AND_USERS_NOT_FOUND));

		List<UserCommonInfoResponse> userResponses = users.stream()
			.map(UserCommonInfoResponse::fromUser)
			.toList();

		return FamilyUserInfoResponse.fromFamily(family, userResponses);
	}




}
