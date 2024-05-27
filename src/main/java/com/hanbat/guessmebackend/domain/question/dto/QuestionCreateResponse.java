package com.hanbat.guessmebackend.domain.question.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.hanbat.guessmebackend.domain.question.entity.Question;

public record QuestionCreateResponse(Long questionId, Long familyId, String content, Boolean isDone, LocalDateTime createdAt) {
	public static QuestionCreateResponse fromQuestion(Question question) {
		return new QuestionCreateResponse(question.getId(), question.getFamily().getId(), question.getContent(), question.getIsDone(), question.getCreatedAt());
	}



}
