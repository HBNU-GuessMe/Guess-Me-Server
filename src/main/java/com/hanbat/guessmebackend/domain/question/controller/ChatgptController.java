package com.hanbat.guessmebackend.domain.question.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.hanbat.guessmebackend.domain.question.application.ChatgptService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/family")
@Slf4j
public class ChatgptController {

	private final ChatgptService chatgptService;

	@PostMapping("/question")
	public ResponseEntity<String> request() {

		return ResponseEntity.ok(chatgptService.generateText("이 질문에 대답해줘!", 1,1000));
	}

	}
