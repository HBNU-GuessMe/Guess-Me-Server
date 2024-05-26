package com.hanbat.guessmebackend.domain.family.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class FamilyInfoRequest {
	private String code;
	private List<Long> userIds;
}
