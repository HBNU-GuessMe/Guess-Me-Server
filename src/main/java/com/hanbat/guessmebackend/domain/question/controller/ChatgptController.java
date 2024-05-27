package com.hanbat.guessmebackend.domain.question.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.guessmebackend.domain.question.application.ChatgptService;
import com.hanbat.guessmebackend.domain.question.application.QuestionService;
import com.hanbat.guessmebackend.domain.question.dto.QuestionCreateResponse;
import com.hanbat.guessmebackend.domain.question.dto.QuestionGetResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/family")
@Slf4j
public class ChatgptController {

	private final ChatgptService chatgptService;
	private final QuestionService questionService;

	/*
		chatgpt로 질문 생성
	 */
	@PostMapping("/question/create")
	public ResponseEntity<List<QuestionCreateResponse>> request() throws JsonProcessingException {
		return ResponseEntity.ok(chatgptService.createQuestion());
	}

	/*
		오늘 질문 조회
	 */
	@GetMapping("/today/question")
	public ResponseEntity<QuestionGetResponse> getTodayQuestion(@RequestParam Long familyId) {
		return ResponseEntity.ok(questionService.getTodayQuestion(familyId));
	}

	}
