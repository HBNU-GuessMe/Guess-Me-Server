package com.hanbat.guessmebackend.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;

public interface CommentQuestionRepository extends JpaRepository<CommentQuestion, Long> {
}
