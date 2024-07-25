package com.hanbat.guessmebackend.domain.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import com.hanbat.guessmebackend.domain.chat.application.ChatService;
import com.hanbat.guessmebackend.domain.chat.dto.Message;
import com.hanbat.guessmebackend.domain.family.entity.Family;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

	private final SimpMessageSendingOperations sendingOperations;
	private final ChatService chatService;



	// 클라이언트에서 메세지 전송
	@CrossOrigin
	@MessageMapping("/channel")
	public void sendMessage(Message message) {
		Long roomId = message.getRoomId();

		sendingOperations.convertAndSend(
			"/sub/channel/" + roomId, message
		);
		log.info("메세지 전송 성공");

		chatService.createChat(message);

		log.info("메세지 저장 성공");
	}


}
