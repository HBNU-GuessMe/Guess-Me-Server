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
		질문에 답변
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
			.build();

		answerRepository.save(answer);
		question.updateAnswerCount(question.getAnswerCount() + 1);
		questionRepository.save(question);

		eventPublisher.publishEvent(answer);

		return AnswerRegisterResponse.fromQuestionAndAnswer(question, answer);
	}

	/*
		현재 사용자의 답변 조회
	 */
	public AnswerGetResponse getOneAnswer(Long questionId) {
		final User user = memberUtil.getCurrentUser();
		Answer answer = answerRepository.findAnswerByQuestionIdAndUserId(questionId, user.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
		return AnswerGetResponse.fromAnswer(user, answer);
	}

	/*
		question이 isPassed면, 가족들의 답변 조회
	 */

	public AnswerGetAllResponse getAllAnswers(Long questionId) {
		Question question = questionRepository.findById(questionId)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		if (!question.getIsPassed()) {
			throw new CustomException(ErrorCode.NOT_PASSED_QUESTION);
		}

		List<Answer> answers = answerRepository.findAnswersByQuestionId(questionId);
		List<AnswerGetResponse> answerResponses = answers.stream()
			.map(answer -> AnswerGetResponse.fromAnswer(answer.getUser(), answer))
			.toList();

		return AnswerGetAllResponse.fromAnswer(question, answerResponses);


	}

}
