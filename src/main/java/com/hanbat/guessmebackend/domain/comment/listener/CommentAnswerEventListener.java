package com.hanbat.guessmebackend.domain.comment.listener;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.guessmebackend.domain.answer.entity.Answer;
import com.hanbat.guessmebackend.domain.chat.application.ChatRoomService;
import com.hanbat.guessmebackend.domain.chat.application.ChatService;
import com.hanbat.guessmebackend.domain.chat.entity.Chatroom;
import com.hanbat.guessmebackend.domain.comment.application.CommentService;
import com.hanbat.guessmebackend.domain.comment.entity.CommentAnswer;
import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;
import com.hanbat.guessmebackend.domain.question.entity.Question;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentAnswerEventListener {
	private final ChatRoomService chatRoomService;
	private final ChatService chatService;
	private final CommentService commentService;
	private final SimpMessagingTemplate simpMessagingTemplate;

	@EventListener
	@Transactional
	public void handleQuestionEvent(CommentAnswer commentAnswer) throws JsonProcessingException {
		Question question = commentAnswer.getCommentQuestion().getQuestion();
		CommentQuestion commentQuestion	= commentAnswer.getCommentQuestion();

		Integer commentAnswerCountSum = commentService.getSumOfCommentAnswerCountForSameFamily(question.getId());
		int familyCount = commentAnswer.getCommentQuestion().getQuestion().getFamily().getCount();
		log.info(String.valueOf(commentAnswerCountSum));
		log.info(String.valueOf(familyCount));
		if (commentAnswerCountSum == familyCount && commentAnswerCountSum == familyCount * 2) {
			commentAnswer.getCommentQuestion().updateIsPassed(true);
			question.updateCommentQuestionCount(question.getCommentQuestionCount() - 1);
		}


		// 카운트가 0이고, isPassed가 true면, 채팅방 생성하고, 채팅 메세지 전송
		if (question.getCommentQuestionCount() == 0 && commentQuestion.getIsPassed())  {
			Chatroom chatroom = chatRoomService.createChatRoom(question.getFamily().getId());
			String content = chatService.sendMessageByServer(chatroom.getId());

			simpMessagingTemplate.convertAndSend(
				"/topic/newChatroom", chatroom.getId()
			);

			// 이후에 클라이언트가 해당 roomId를 구독한 후 메시지를 받을 수 있도록 잠시 대기
			try {
				Thread.sleep(40000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			simpMessagingTemplate.convertAndSend(
				"/sub/channel/" + chatroom.getId(), content
			);

			log.info("해피 속마음채팅 첫 메세지 전송 성공");
		}

	}
}

