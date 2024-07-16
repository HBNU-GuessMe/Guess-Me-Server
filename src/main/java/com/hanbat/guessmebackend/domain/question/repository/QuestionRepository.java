package com.hanbat.guessmebackend.domain.question.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanbat.guessmebackend.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	@Query("SELECT q FROM Question q JOIN q.family f WHERE f.id = :familyId AND q.publishedAt IS NULL ORDER BY RAND() LIMIT 1")
	Optional<Question> findRandomQuestionByFamilyId(@Param("familyId") Long familyId);

	@Query("SELECT q FROM Question q JOIN FETCH q.family WHERE q.id = :questionId")
	Optional<Question> findFamilyQuestionById(@Param("questionId") Long questionId);
}
