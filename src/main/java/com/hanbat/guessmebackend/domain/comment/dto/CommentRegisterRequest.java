package com.hanbat.guessmebackend.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentRegisterRequest {
	private Long commentQuestionId;
	private String commentAnswerContent;
}
