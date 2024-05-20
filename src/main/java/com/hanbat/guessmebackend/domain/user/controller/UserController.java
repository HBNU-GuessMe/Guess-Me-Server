package com.hanbat.guessmebackend.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanbat.guessmebackend.domain.user.application.UserService;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoRequest;
import com.hanbat.guessmebackend.domain.user.dto.UserCommonInfoResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
	private final UserService userService;

	@PostMapping("/info/register")
	public ResponseEntity<UserCommonInfoResponse> registerInfo(@RequestBody @Valid UserCommonInfoRequest userCommonInfoRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.postUserInfo(userCommonInfoRequest));
	}

}
