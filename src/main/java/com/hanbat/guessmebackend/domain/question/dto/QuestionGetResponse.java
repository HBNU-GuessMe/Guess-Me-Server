package com.hanbat.guessmebackend.domain.question.dto;

import java.time.LocalDateTime;

import com.hanbat.guessmebackend.domain.question.entity.Question;

public record QuestionGetResponse(Long questionId, Long familyId, String content, Boolean isPassed, LocalDateTime publishDate) {
	public static QuestionGetResponse fromQuestion(Question question) {
		return new QuestionGetResponse(question.getId(), question.getFamily().getId(), question.getContent(), question.getIsPassed(), question.getPublishedAt());
	}
}
