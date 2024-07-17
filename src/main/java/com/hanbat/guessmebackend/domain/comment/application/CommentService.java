package com.hanbat.guessmebackend.domain.comment.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.answer.dto.AnswerRegisterRequest;
import com.hanbat.guessmebackend.domain.answer.dto.AnswerRegisterResponse;
import com.hanbat.guessmebackend.domain.answer.entity.Answer;
import com.hanbat.guessmebackend.domain.comment.dto.CommentRegisterRequest;
import com.hanbat.guessmebackend.domain.comment.dto.CommentRegisterResponse;
import com.hanbat.guessmebackend.domain.comment.entity.CommentAnswer;
import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;
import com.hanbat.guessmebackend.domain.comment.repository.CommentAnswerRepository;
import com.hanbat.guessmebackend.domain.comment.repository.CommentQuestionRepository;
import com.hanbat.guessmebackend.domain.question.entity.Question;
import com.hanbat.guessmebackend.domain.user.entity.User;
import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;
import com.hanbat.guessmebackend.global.jwt.MemberUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentService {

	private final MemberUtil memberUtil;
	private final ApplicationEventPublisher eventPublisher;
	private final CommentQuestionRepository commentQuestionRepository;
	private final CommentAnswerRepository commentAnswerRepository;

	/*
		댓글 질문에 답변
	 */
	@Transactional
	public CommentRegisterResponse answerCommentQuestion(CommentRegisterRequest commentRegisterRequest) {

		final User user = memberUtil.getCurrentUser();

		CommentQuestion commentQuestion = commentQuestionRepository.findById(commentRegisterRequest.getCommentQuestionId())
			.orElseThrow(() -> new CustomException(ErrorCode.COMMENT_QUESTION_NOT_FOUND));

		CommentAnswer commentAnswer = CommentAnswer.builder()
			.commentQuestion(commentQuestion)
			.content(commentRegisterRequest.getCommentAnswerContent())
			.build();

		commentAnswerRepository.save(commentAnswer);
		commentQuestion.updateCommentAnswerCount(commentQuestion.getCommentAnswerCount() + 1);
		eventPublisher.publishEvent(commentAnswer);


		return CommentRegisterResponse.fromQuestionAndAnswer(commentQuestion, commentAnswer) ;
	}



}
