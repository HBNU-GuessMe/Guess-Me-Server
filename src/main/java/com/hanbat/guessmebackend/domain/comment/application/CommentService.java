package com.hanbat.guessmebackend.domain.comment.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.hanbat.guessmebackend.domain.comment.dto.CommentQuestionAndAnswerGetResponse;
import com.hanbat.guessmebackend.domain.comment.dto.CommentQuestionRegisterResponse;
import com.hanbat.guessmebackend.domain.comment.dto.CommentRegisterRequest;
import com.hanbat.guessmebackend.domain.comment.dto.CommentRegisterResponse;
import com.hanbat.guessmebackend.domain.comment.dto.CommentsByFamilyGetResponse;
import com.hanbat.guessmebackend.domain.comment.entity.CommentAnswer;
import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;
import com.hanbat.guessmebackend.domain.comment.repository.CommentAnswerRepository;
import com.hanbat.guessmebackend.domain.comment.repository.CommentQuestionRepository;
import com.hanbat.guessmebackend.domain.family.entity.Family;
import com.hanbat.guessmebackend.domain.family.repository.FamilyRepository;
import com.hanbat.guessmebackend.domain.question.dto.QuestionCreateResponse;
import com.hanbat.guessmebackend.domain.question.entity.Question;
import com.hanbat.guessmebackend.domain.question.repository.QuestionRepository;
import com.hanbat.guessmebackend.domain.user.entity.User;
import com.hanbat.guessmebackend.domain.user.repository.UserRepository;
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
	private final QuestionRepository questionRepository;
	private final UserRepository userRepository;
	private final FamilyRepository familyRepository;

	/*
		댓글 질문에 답변
	 */
	@Transactional
	public CommentRegisterResponse answerCommentQuestion(CommentRegisterRequest commentRegisterRequest) {

		CommentQuestion commentQuestion = commentQuestionRepository.findById(commentRegisterRequest.getCommentQuestionId())
			.orElseThrow(() -> new CustomException(ErrorCode.COMMENT_QUESTION_NOT_FOUND));

		final User user = memberUtil.getCurrentUser();
		if (!user.getId().equals(commentQuestion.getUser().getId())) {
			throw new CustomException(ErrorCode.COMMENT_QUESTION_NOT_OWNER);
		}

		CommentAnswer commentAnswer = new CommentAnswer(commentRegisterRequest.getCommentAnswerContent());

		commentAnswerRepository.save(commentAnswer);
		commentQuestion.updateCommentAnswerCount(commentQuestion.getCommentAnswerCount() + 1);
		commentQuestion.updateCommentAnswer(commentAnswer);

		eventPublisher.publishEvent(commentAnswer);


		return CommentRegisterResponse.fromQuestionAndAnswer(commentQuestion, commentAnswer) ;
	}

	/*
		댓글 질문과 답변들 조회
	 */
	public List<CommentQuestionAndAnswerGetResponse> getCommentQuestionAndAnswer(Long questionId) {
		final User user = memberUtil.getCurrentUser();
		Question question = questionRepository.findById(questionId)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		List<CommentQuestionAndAnswerGetResponse> responses = new ArrayList<>();
		// 기본 질문 1개 조회
		CommentQuestion baseCommentQuestion = commentQuestionRepository.findFirstByQuestionIdAndUserId(questionId, user.getId());
		baseCommentQuestion.updatePublishedAt(LocalDateTime.now());
		// 답변이 있을 경우, 답변도 조회
		Optional<CommentAnswer> commentAnswer = commentAnswerRepository.findByCommentQuestion(baseCommentQuestion);
		if (commentAnswer.isPresent()) {
			responses.add(CommentQuestionAndAnswerGetResponse.fromCommentQuestion(baseCommentQuestion, commentAnswer.get()));
		} else {
			responses.add(CommentQuestionAndAnswerGetResponse.fromCommentQuestion(baseCommentQuestion, null));
		}

		// 기본 질문의 isPassed가 true인 경우, published_at이 null인 추가 질문 조회
		if (baseCommentQuestion.getIsPassed()) {
			CommentQuestion additionalCommentQuestion = commentQuestionRepository.findFirstByQuestionIdAndUserId(questionId, user.getId());
			additionalCommentQuestion.updatePublishedAt(LocalDateTime.now());
			// 추가 질문의 답변도 있을 경우, 답변도 조회
			Optional<CommentAnswer> additionalCommentAnswer = commentAnswerRepository.findByCommentQuestion(additionalCommentQuestion);
			if (additionalCommentAnswer.isPresent()) {
				responses.add(CommentQuestionAndAnswerGetResponse.fromCommentQuestion(additionalCommentQuestion, additionalCommentAnswer.get()));
			} else {
				responses.add(CommentQuestionAndAnswerGetResponse.fromCommentQuestion(additionalCommentQuestion, null));
			}

		}

		return responses;
	}

	/*
		가족 구성원들의 모든 댓글 질문과 댓글 질문 답변 조회
	 */
	public List<CommentsByFamilyGetResponse> getAllCommentsByFamily(Long familyId) {
		Family family = familyRepository.findById(familyId)
			.orElseThrow(() -> new CustomException(ErrorCode.FAMILY_NOT_FOUND));

		List<Object[]> results = commentQuestionRepository.findCommentQuestionsAndAnswersByFamilyId(familyId);
		return results.stream().map(result -> {
			CommentQuestion commentQuestion = (CommentQuestion) result[0];
			CommentAnswer commentAnswer = (CommentAnswer) result[1];

			CommentsByFamilyGetResponse response = CommentsByFamilyGetResponse.builder()
				.userId(commentQuestion.getUser().getId())
				.userName(commentQuestion.getUser().getNickname())
				.commentQuestionContent(commentQuestion.getContent())
				.commentAnswerContent(commentAnswer.getContent() )
				.build();

			return response;
		}).collect(Collectors.toList());
	}

	/*
		chatgpt API 요청해서 받은 response 파싱해서 댓글 질문 등록
 	*/
	@Transactional
	public List<CommentQuestionRegisterResponse> registerCommentQuestion(JsonNode rootNode) {

		JsonNode jsonData = rootNode.get("data");
		log.info(jsonData.toString());

		Long questionId = jsonData.get("questionId").asLong();
		log.info(questionId.toString());
		Question question = questionRepository.findById(questionId)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		JsonNode jsonQuestions = jsonData.get("commentQuestions");

		List<CommentQuestionRegisterResponse> responses = new ArrayList<>();
		for (JsonNode jsonNode : jsonQuestions) {
			log.info(jsonNode.toString());

			Long userId = jsonNode.get("userId").asLong();
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
			log.info(userId.toString());

			String questionContent = jsonNode.get("questionContent").asText();
			log.info(questionContent);

			CommentQuestion commentQuestion = CommentQuestion.builder()
					.user(user)
					.question(question)
					.content(questionContent)
				    .isPassed(false)
					.build();

			commentQuestionRepository.save(commentQuestion);

			responses.add(CommentQuestionRegisterResponse.fromCommentQuestion(commentQuestion));
		}

		return responses;
	}

	// 같은 질문이고, 같은 가족 구성원의 댓글 질문 답변 카운트 수 구하기
	public Integer getSumOfCommentAnswerCountForSameFamily(Long questionId) {
		return commentQuestionRepository.findSumOfCommentAnswerCount(questionId);
	}

	public List<CommentQuestion> findCommentQuestionAlreadyPublished(Long questionId) {
		return commentQuestionRepository.findCommentQuestionAlreadyPublished(questionId);
	}






}
