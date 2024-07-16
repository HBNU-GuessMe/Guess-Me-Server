package com.hanbat.guessmebackend.domain.chat.listener;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.hanbat.guessmebackend.domain.chat.application.ChatRoomService;
import com.hanbat.guessmebackend.domain.chat.application.ChatService;
import com.hanbat.guessmebackend.domain.chat.entity.Chatroom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatEventListener {
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ChatService chatService;
	private final ChatRoomService chatRoomService;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


	/*
		댓글질문이 패스될 때마다 count를 감소시키고, count가 0이 될 때
		채팅방 생성 후, 서버에서 메세지전송(해피)하는 이벤트 확인
	 */
	@EventListener
	@Transactional
	public void handleChatEvent(Long familyId) {
		Chatroom chatroom = chatRoomService.createChatRoom(familyId);
		String content = chatService.sendMessageByServer(chatroom.getId());

		simpMessagingTemplate.convertAndSend(
			"/sub/channel/" + chatroom.getId(), content
		);

		log.info("해피 속마음채팅 첫 메세지 전송 성공");
	}


	@EventListener
	@Transactional
	public void handleChatRoomEvent(Chatroom chatroom) {
		log.info("이벤트 시작");
		scheduler.schedule(() -> {
			chatService.sendEndMessage(chatroom.getId());
			log.info("메서드 수행확인");
			scheduler.shutdown();
		}, 30, TimeUnit.SECONDS);

	}
}


