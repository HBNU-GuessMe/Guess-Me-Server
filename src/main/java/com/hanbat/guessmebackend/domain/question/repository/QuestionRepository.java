package com.hanbat.guessmebackend.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbat.guessmebackend.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
