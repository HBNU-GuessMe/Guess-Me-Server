package com.hanbat.guessmebackend.domain.family.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanbat.guessmebackend.domain.family.application.FamilyService;
import com.hanbat.guessmebackend.domain.family.dto.FamilyInfoRequest;
import com.hanbat.guessmebackend.domain.family.dto.FamilyInfoResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/family")
@Slf4j
public class FamilyController {
	private final FamilyService familyService;

	/*
		코드 주인이 '모든 가족이 연결되었습니까?' 버튼을 클릭시 가족 테이블에 저장
	 */

	@PostMapping("/connection")
	public ResponseEntity<FamilyInfoResponse> connectFamily(@RequestBody FamilyInfoRequest familyInfoRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(familyService.connectFamily(familyInfoRequest));
	}
}
