package com.hanbat.guessmebackend.domain.comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbat.guessmebackend.domain.comment.entity.CommentAnswer;
import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;

public interface CommentAnswerRepository extends JpaRepository<CommentAnswer, Long> {

	Optional<CommentAnswer> findByCommentQuestion(CommentQuestion commentQuestion);
}
