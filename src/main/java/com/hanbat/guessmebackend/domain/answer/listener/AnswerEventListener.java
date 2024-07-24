package com.hanbat.guessmebackend.domain.answer.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.comment.entity.CommentAnswer;
import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;

@Component
public class AnswerEventListener {
	/*
		댓글 질문 답변할 때마다 답변 카운트 수와 가족 구성원의 수가 같은지 확인 후, 같으면 isPassed를 true로 변경
		만약, 댓글 질문 답변 수정하면, content를 수정하므로 카운트 수 동일
	 */
	@EventListener
	@Transactional
	public void handleQuestionEvent(CommentQuestion commentQuestion) {
		if (commentQuestion.getCommentAnswerCount() == commentQuestion.getQuestion().getFamily().getCount()){
			commentQuestion.updateIsPassed(true);
		}
	}


}
