package com.hanbat.guessmebackend.domain.question.listener;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.answer.entity.Answer;
import com.hanbat.guessmebackend.domain.answer.repository.AnswerRepository;
import com.hanbat.guessmebackend.domain.question.entity.Question;
import com.hanbat.guessmebackend.domain.question.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionEventListener {

	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;

	/*
		(추후 개발 : 24시간이 다 지나거나) 가족이 다 답변을 완료했을 때 isDone을 true로 변경
	 */
	@EventListener
	@Transactional
	public void handleQuestionEvent(Question question) {
		List<Answer> answers = answerRepository.findAnswersByQuestionId(question.getId());
		answers.stream().forEach( (answer -> {
			if (answer.getQuestion().getFamily().getCount() == answers.size()) {
				if (answers.stream().allMatch(Answer::getIsDone)) {
					question.updateIsDone(true);
					questionRepository.save(question);
				}

			}
			})
		);

	}
}
