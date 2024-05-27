package com.hanbat.guessmebackend.domain.answer.dto;

import lombok.Getter;

@Getter
public class AnswerRegisterRequest {
	private Long questionId;
	private String content;
}
