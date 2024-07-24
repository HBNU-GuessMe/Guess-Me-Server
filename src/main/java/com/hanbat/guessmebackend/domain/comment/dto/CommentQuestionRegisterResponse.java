package com.hanbat.guessmebackend.domain.comment.dto;


import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;

public record CommentQuestionRegisterResponse(Long commentId, Long userId, String userName, String commentQuestionContent) {
	public static CommentQuestionRegisterResponse fromCommentQuestion(CommentQuestion commentQuestion) {
		return new CommentQuestionRegisterResponse(commentQuestion.getId(), commentQuestion.getUser().getId(),
			commentQuestion.getUser().getNickname()
			, commentQuestion.getContent());
	}
}

