package com.hanbat.guessmebackend.domain.answer.dto;


import com.hanbat.guessmebackend.domain.answer.entity.Answer;
import com.hanbat.guessmebackend.domain.question.entity.Question;

public record AnswerRegisterResponse(Long questionId, String questionContent, Boolean questionPassed, Long answerId, String answerContent, Long familyId, Long userId, String nickname) {
	public static AnswerRegisterResponse fromQuestionAndAnswer(Question question, Answer answer) {
		return new AnswerRegisterResponse(
			question.getId(), question.getContent(), question.getIsPassed(), answer.getId(), answer.getContent(), question.getFamily().getId(), answer.getUser().getId(), answer.getUser().getNickname()

		);
	}
}
