package com.hanbat.guessmebackend.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanbat.guessmebackend.domain.user.application.UserService;
import com.hanbat.guessmebackend.domain.user.dto.CodeInputResponse;
import com.hanbat.guessmebackend.domain.user.dto.CodeResponse;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoRequest;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
	private final UserService userService;

	/*
		유저 기본 정보 등록
	 */

	@PostMapping("/info/register")
	public ResponseEntity<UserCommonInfoResponse> registerInfo(@RequestBody @Valid UserCommonInfoRequest userCommonInfoRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.postUserInfo(userCommonInfoRequest));
	}

	/*
		유저 코드 조회
	 */
	@GetMapping("/code")
	public ResponseEntity<CodeResponse> getCode() {
		return ResponseEntity.ok(userService.getCode());
	}

	/*
		입력한 코드가 유저테이블에 있는 코드인지 확인, 코드가 일치하는지 확인
	 */
	@GetMapping("/code/input/validate")
	public ResponseEntity<CodeInputResponse> validateCode(@RequestParam @NotNull String code) {
		return ResponseEntity.ok(userService.validateCode(code));
	}

}
