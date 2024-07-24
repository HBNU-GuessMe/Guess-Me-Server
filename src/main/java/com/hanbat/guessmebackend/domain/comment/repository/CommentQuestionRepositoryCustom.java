package com.hanbat.guessmebackend.domain.comment.repository;

import java.util.Optional;

import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;

public interface CommentQuestionRepositoryCustom {
	CommentQuestion findFirstByQuestionIdAndUserId(Long questionId, Long userId);
}

