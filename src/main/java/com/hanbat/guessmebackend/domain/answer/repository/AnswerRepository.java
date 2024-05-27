package com.hanbat.guessmebackend.domain.answer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanbat.guessmebackend.domain.answer.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
	@Query("SELECT a FROM Answer a JOIN a.question q WHERE q.id = :questionId")
	List<Answer> findAnswersByQuestionId(@Param("questionId") Long questionId);

	@Query("SELECT a FROM Answer a JOIN a.question q JOIN a.user u WHERE q.id = :questionId AND u.id = :userId")
	Optional<Answer> findAnswerByQuestionIdAndUserId(@Param("questionId") Long questionId, @Param("userId") Long userId);

}
