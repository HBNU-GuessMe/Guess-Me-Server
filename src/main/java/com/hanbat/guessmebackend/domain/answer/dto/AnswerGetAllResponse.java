package com.hanbat.guessmebackend.domain.answer.dto;

import java.util.List;

import com.hanbat.guessmebackend.domain.question.entity.Question;

public record AnswerGetAllResponse(Long questionId, String questionContent, Long familyId, List<AnswerGetResponse> answers) {

	public static AnswerGetAllResponse fromAnswer(Question question, List<AnswerGetResponse> answers) {
		return new AnswerGetAllResponse(question.getId(), question.getContent(), question.getFamily().getId(), answers);
	}
}
