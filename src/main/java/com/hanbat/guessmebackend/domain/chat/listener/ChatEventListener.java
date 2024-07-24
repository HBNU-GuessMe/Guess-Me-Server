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


