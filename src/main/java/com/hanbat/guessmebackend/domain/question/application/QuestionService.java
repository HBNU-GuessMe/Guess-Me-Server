package com.hanbat.guessmebackend.domain.question.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.question.dto.QuestionGetResponse;
import com.hanbat.guessmebackend.domain.question.entity.Question;
import com.hanbat.guessmebackend.domain.question.repository.QuestionRepository;
import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class QuestionService {
	private final QuestionRepository questionRepository;

	/*
		familyId가 같은 것 중에서 랜덤으로 고르기
	 */
	public QuestionGetResponse getTodayQuestion(Long familyId) {
		Question question = questionRepository.findRandomQuestionByFamilyId(familyId)
			.orElseThrow( () -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
		question.updatePublishDate(LocalDateTime.now());
		questionRepository.save(question);
		return QuestionGetResponse.fromQuestion(question);
	}


}
