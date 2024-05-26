package com.hanbat.guessmebackend.domain.family.dto;

import java.util.List;

import com.hanbat.guessmebackend.domain.family.entity.Family;

public record FamilyInfoResponse(Long familyId, String familyCode, int count, List<Long> userIds) {

	public static FamilyInfoResponse fromFamily(Family family, List<Long> userIds) {
		return new FamilyInfoResponse(
			family.getId(), family.getFamilyCode(), family.getCount(), userIds

		);
	}
}
