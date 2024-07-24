package com.hanbat.guessmebackend.domain.answer.dto;

import com.hanbat.guessmebackend.domain.answer.entity.Answer;
import com.hanbat.guessmebackend.domain.user.entity.User;

public record AnswerGetResponse(Long userId, String userName, Long answerId, String content) {

	public static AnswerGetResponse fromAnswer(User user, Answer answer) {
		return new AnswerGetResponse(user.getId(), user.getNickname(), answer.getId(), answer.getContent());
	}
}
