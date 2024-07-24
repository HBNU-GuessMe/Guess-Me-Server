package com.hanbat.guessmebackend.domain.comment.dto;

import com.hanbat.guessmebackend.domain.comment.entity.CommentAnswer;
import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;

public record CommentRegisterResponse(Long commentQuestionId, String commentQuestionContent, Boolean commentQuestionPassed, Long commentAnswerId, String commentAnswerContent, Long userId, String nickname) {
	public static CommentRegisterResponse fromQuestionAndAnswer(
		CommentQuestion question, CommentAnswer answer) {
		return new CommentRegisterResponse(
			question.getId(), question.getContent(), question.getIsPassed(), answer.getId(), answer.getContent(), question.getUser().getId(), question.getUser().getNickname()

		);
	}
}
