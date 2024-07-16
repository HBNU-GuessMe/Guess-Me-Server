package com.hanbat.guessmebackend.domain.family.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanbat.guessmebackend.domain.family.application.FamilyService;
import com.hanbat.guessmebackend.domain.family.dto.FamilyConnectionRequest;
import com.hanbat.guessmebackend.domain.family.dto.FamilyConnectionWithOwnerRequest;
import com.hanbat.guessmebackend.domain.family.dto.FamilyUserInfoResponse;

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

	@PostMapping("/owner/connection")
	public ResponseEntity<FamilyUserInfoResponse> connectFamilyWithOwner(@RequestBody FamilyConnectionWithOwnerRequest familyConnectionWithOwnerRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(familyService.connectFamilyWithOwner(
			familyConnectionWithOwnerRequest));
	}

	/*
		추가 가족 구성원이 가족 연결
	 */
	@PostMapping("/connection")
	public ResponseEntity<FamilyUserInfoResponse> connectFamily(@RequestBody FamilyConnectionRequest familyConnectionRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(familyService.connectFamily(
			familyConnectionRequest));
	}

	/*
		가족 정보 조회
	 */
	@GetMapping("/info/{familyId}")
	public ResponseEntity<FamilyUserInfoResponse> getFamilyInfo(@PathVariable Long familyId) {
		return ResponseEntity.ok(familyService.getFamilyInfo(familyId));
	}
}
