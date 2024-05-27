package com.hanbat.guessmebackend.domain.answer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanbat.guessmebackend.domain.answer.application.AnswerService;
import com.hanbat.guessmebackend.domain.answer.dto.AnswerGetAllResponse;
import com.hanbat.guessmebackend.domain.answer.dto.AnswerGetResponse;
import com.hanbat.guessmebackend.domain.answer.dto.AnswerRegisterRequest;
import com.hanbat.guessmebackend.domain.answer.dto.AnswerRegisterResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answer")
@Slf4j
public class AnswerController {

	private final AnswerService answerService;

	/*
		질문에 대한 답변
	 */
	@PostMapping("/register")
	public ResponseEntity<AnswerRegisterResponse> answerQuestion(@RequestBody @Valid AnswerRegisterRequest answerRegisterRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(answerService.answerQuestion(answerRegisterRequest));
	}

	/*
		유저 한사람의 답변 확인
	 */
	@GetMapping("/get")
	public ResponseEntity<AnswerGetResponse> getAnswer(@RequestParam Long questionId) {
		return ResponseEntity.ok(answerService.getOneAnswer(questionId));
	}

	/*
		유저들 전체의 답변 확인
	 */
	@GetMapping("/getAll")
	public ResponseEntity<AnswerGetAllResponse> getAllAnswers(@RequestParam Long questionId) {
		return ResponseEntity.ok(answerService.getAllAnswers(questionId));
	}

}
