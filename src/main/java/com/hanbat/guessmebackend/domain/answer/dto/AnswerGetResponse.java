package com.hanbat.guessmebackend.domain.answer.dto;

import com.hanbat.guessmebackend.domain.answer.entity.Answer;

public record AnswerGetResponse(Long userId, Long answerId, String content) {

	public static AnswerGetResponse fromAnswer(Long userId, Answer answer) {
		return new AnswerGetResponse(userId, answer.getId(), answer.getContent());
	}
}
