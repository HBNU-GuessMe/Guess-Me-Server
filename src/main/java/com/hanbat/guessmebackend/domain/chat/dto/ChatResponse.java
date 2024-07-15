package com.hanbat.guessmebackend.domain.chat.dto;

import com.hanbat.guessmebackend.domain.chat.entity.Chat;

public record ChatResponse(String chatId, Long roomId, Long userId, String nickname, String senderType, String content){

	public static ChatResponse fromChat(Chat chat, String nickname) {
		return new ChatResponse(
			chat.getId(), chat.getRoomId(), chat.getUserId(), nickname, chat.getSenderType(), chat.getContent()
		);
	}
}
