package com.hanbat.guessmebackend.domain.chat.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanbat.guessmebackend.domain.chat.application.ChatService;
import com.hanbat.guessmebackend.domain.chat.dto.ChatResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
@Slf4j
public class ChatRoomMessageController {

	private final ChatService chatService;

	@GetMapping("/messages/{chatroomId}")
	public List<ChatResponse> getMessages(@PathVariable Long chatroomId) {
		return chatService.getChats(chatroomId);
	}
}
