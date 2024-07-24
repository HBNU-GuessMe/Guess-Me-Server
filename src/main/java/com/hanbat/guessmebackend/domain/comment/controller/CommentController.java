package com.hanbat.guessmebackend.domain.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanbat.guessmebackend.domain.comment.application.CommentService;
import com.hanbat.guessmebackend.domain.comment.dto.CommentQuestionAndAnswerGetResponse;
import com.hanbat.guessmebackend.domain.comment.dto.CommentRegisterRequest;
import com.hanbat.guessmebackend.domain.comment.dto.CommentRegisterResponse;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/register")
	public ResponseEntity<CommentRegisterResponse> registerComment(@RequestBody CommentRegisterRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(commentService.answerCommentQuestion(request));
	}

	@GetMapping("/get")
	public ResponseEntity<List<CommentQuestionAndAnswerGetResponse>> getCommentQuestionAndAnswer(@RequestParam @NotNull Long questionId) {
		return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentQuestionAndAnswer(questionId));
	}
}
