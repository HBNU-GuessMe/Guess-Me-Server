package com.hanbat.guessmebackend.domain.question.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.hanbat.guessmebackend.domain.question.entity.Question;

public record QuestionCreateResponse(Long questionId, Long familyId, String content, Boolean isPassed, LocalDateTime createdAt) {
	public static QuestionCreateResponse fromQuestion(Question question) {
		return new QuestionCreateResponse(question.getId(), question.getFamily().getId(), question.getContent(), question.getIsPassed(), question.getCreatedAt());
	}



}
