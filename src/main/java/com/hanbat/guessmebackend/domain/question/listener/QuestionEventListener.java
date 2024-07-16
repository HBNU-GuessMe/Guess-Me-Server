package com.hanbat.guessmebackend.domain.question.listener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.answer.entity.Answer;
import com.hanbat.guessmebackend.domain.answer.repository.AnswerRepository;
import com.hanbat.guessmebackend.domain.question.entity.Question;
import com.hanbat.guessmebackend.domain.question.repository.QuestionRepository;
import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
public class QuestionEventListener {
	/*
		답변할 때마다 답변 카운트 수와 가족 구성원의 수가 같은지 확인 후, 같으면 isPassed를 true로 변경
		만약, 답변 수정하면, content를 수정하므로 카운트 수 동일
	 */
	@EventListener
	@Transactional
	public void handleQuestionEvent(Answer answer) {
	if (answer.getQuestion().getAnswerCount() == answer.getQuestion().getFamily().getCount()){
			answer.getQuestion().updateIsPassed(true);
		}
	}


}
