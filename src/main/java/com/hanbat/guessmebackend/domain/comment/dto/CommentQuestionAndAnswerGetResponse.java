package com.hanbat.guessmebackend.domain.comment.dto;


import java.util.List;
import java.util.Optional;

import com.hanbat.guessmebackend.domain.comment.entity.CommentAnswer;
import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;

public record CommentQuestionAndAnswerGetResponse(Long userId, Long commentQuestionId, String commentQuestionContent, int count, Boolean isPassed,
												  CommentAnswer commentAnswer) {
	public static CommentQuestionAndAnswerGetResponse fromCommentQuestion(CommentQuestion commentQuestion, CommentAnswer commentAnswer) {
		return new CommentQuestionAndAnswerGetResponse(commentQuestion.getUser().getId(), commentQuestion.getId(), commentQuestion.getContent(),
			commentQuestion.getQuestion().getCommentQuestionCount(), commentQuestion.getIsPassed(), commentAnswer
			);
	}
}
