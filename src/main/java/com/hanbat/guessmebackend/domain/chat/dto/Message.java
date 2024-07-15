package com.hanbat.guessmebackend.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Message {
	private Long roomId;
	private Long senderId;
	private String nickname;
	private String content;

	public Message(Long roomId, Long senderId, String nickname, String content) {
		this.roomId = roomId;
		this.senderId = senderId;
		this.nickname = nickname;
		this.content = content;
	}
}
