package com.hanbat.guessmebackend.domain.question.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.answer.entity.Answer;
import com.hanbat.guessmebackend.domain.answer.repository.AnswerRepository;
import com.hanbat.guessmebackend.domain.family.repository.FamilyRepository;
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
	private final AnswerRepository answerRepository;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> scheduledTask;

	/*
		familyId가 같은 것 중에서 랜덤으로 골라서 조회 (publish)
	 */
	public QuestionGetResponse getTodayQuestion(Long familyId) {
		Question question = questionRepository.findRandomQuestionByFamilyId(familyId)
			.orElseThrow( () -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
		question.updatePublishedAt(LocalDateTime.now());

		// 24시간마다 가족 답변 상태 확인 (테스트를 위해 5분으로)
		scheduledTask = scheduler.scheduleWithFixedDelay(checkQuestionStatusRunnable(question.getId()), 0, 30, TimeUnit.SECONDS);

		return QuestionGetResponse.fromQuestion(question);
	}

	/*
		24시간이 지나고, 가족 과반수 이상 답변했을 때 isPassed을 true로 변경, scheduler 종료
		24시간이 지나도 가족 과반수 미만 답변했을 때는 isPassed 여전히 false, scheduler 계속 동작
	 */

	public Runnable checkQuestionStatusRunnable(Long questionId) {
		return new Runnable() {
			@Override
			public void run() {
				log.info("Check question status");
				// 로그는 되는데 그 밑에 코드를 쓰면 실행이 안됨

				try {
					Question question = questionRepository.findFamilyQuestionById(questionId)
						.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

					List<Answer> answers = answerRepository.findAnswersByQuestionId(questionId);
					log.info(String.valueOf(answers.size()));
					log.info(question.getContent());

					// Lazy Loading 조심 -> fetch Join으로 해결
					int count = question.getFamily().getCount();
					log.info(String.valueOf(count));

					if (question.getFamily().getCount() / 2 + 1 < answers.size()) {
						log.info("가족 과반수 이상 답변을 완료했습니다.");
						question.updateIsPassed(true);
					}

					// isPassed가 true면 주기적으로 확인하는 작업 stop
					if (question.getIsPassed()) {
						log.info("스케줄 처리를 멈춥니다.");
						scheduledTask.cancel(false);
						scheduler.shutdown();
					}

				} catch(Exception e){
					log.error(String.valueOf(e));
				}

			}
		};
	}



}
