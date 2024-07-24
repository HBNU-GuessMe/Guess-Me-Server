package com.hanbat.guessmebackend.domain.comment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentsByFamilyGetResponse {
	private Long userId;
	private String userName;
	private String CommentQuestionContent;
	private String commentAnswerContent;


	@Builder
	public CommentsByFamilyGetResponse(Long userId, String userName, String commentQuestionContent, String commentAnswerContent) {
		this.userId = userId;
		this.userName = userName;
		this.CommentQuestionContent = commentQuestionContent;
		this.commentAnswerContent = commentAnswerContent;

	}
}
