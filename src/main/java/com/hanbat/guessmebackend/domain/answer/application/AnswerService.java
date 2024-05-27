package com.hanbat.guessmebackend.domain.answer.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.answer.dto.AnswerGetAllResponse;
import com.hanbat.guessmebackend.domain.answer.dto.AnswerGetResponse;
import com.hanbat.guessmebackend.domain.answer.dto.AnswerRegisterRequest;
import com.hanbat.guessmebackend.domain.answer.dto.AnswerRegisterResponse;
import com.hanbat.guessmebackend.domain.answer.entity.Answer;
import com.hanbat.guessmebackend.domain.answer.repository.AnswerRepository;
import com.hanbat.guessmebackend.domain.question.entity.Question;
import com.hanbat.guessmebackend.domain.question.repository.QuestionRepository;
import com.hanbat.guessmebackend.domain.user.entity.User;
import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;
import com.hanbat.guessmebackend.global.jwt.MemberUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {
	private final AnswerRepository answerRepository;
	private final QuestionRepository questionRepository;
	private final MemberUtil memberUtil;
	private final ApplicationEventPublisher eventPublisher;

	/*
		답변할 때마다 이벤트 트리거로 확인
	 */
	@Transactional
	public AnswerRegisterResponse answerQuestion(AnswerRegisterRequest answerRegisterRequest) {

		final User user = memberUtil.getCurrentUser();

		Question question = questionRepository.findById(answerRegisterRequest.getQuestionId())
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		Answer answer = Answer.builder()
			.question(question)
			.user(user)
			.content(answerRegisterRequest.getContent())
			.isDone(true)
			.build();

		answerRepository.save(answer);

		eventPublisher.publishEvent(question);

		return AnswerRegisterResponse.fromQuestionAndAnswer(question, answer);
	}


}
