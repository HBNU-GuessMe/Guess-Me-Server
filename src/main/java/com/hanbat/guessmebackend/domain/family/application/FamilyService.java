package com.hanbat.guessmebackend.domain.family.application;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.family.dto.FamilyInfoRequest;
import com.hanbat.guessmebackend.domain.family.dto.FamilyInfoResponse;
import com.hanbat.guessmebackend.domain.family.entity.Family;
import com.hanbat.guessmebackend.domain.family.repository.FamilyRepository;
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

	@Transactional
	public FamilyInfoResponse connectFamily(FamilyInfoRequest familyInfoRequest) {

		int count = familyInfoRequest.getUserIds().size();

		Family family = Family.builder()
			.familyCode(familyInfoRequest.getCode())
			.count(count)
			.build();

		familyRepository.save(family);

		familyInfoRequest.getUserIds().forEach(userId -> {
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
			user.updateFamily(family);
			userRepository.save(user);
		});


		return FamilyInfoResponse.fromFamily(family, familyInfoRequest.getUserIds());

	}
}
