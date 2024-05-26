package com.hanbat.guessmebackend.domain.family.dto;

import java.util.List;

import com.hanbat.guessmebackend.domain.family.entity.Family;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoResponse;

public record FamilyUserInfoResponse(Long familyId, String familyCode, int count, List<UserCommonInfoResponse> users) {
	public static FamilyUserInfoResponse fromFamily(Family family, List<UserCommonInfoResponse> users) {
		return new FamilyUserInfoResponse(
			family.getId(), family.getFamilyCode(), family.getCount(), users

		);
	}
}
