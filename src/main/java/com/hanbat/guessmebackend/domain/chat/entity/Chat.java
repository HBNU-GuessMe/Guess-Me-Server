package com.hanbat.guessmebackend.domain.chat.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collation = "chat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
	@Id
	@Field(targetType = FieldType.OBJECT_ID)
	private String id;

	@Field(name = "room_id")
	private Long roomId;

	@Field(name = "user_id")
	private Long userId;

	@Field(name = "sender_type")
	private String senderType;

	private String content;

	private LocalDateTime sentAt;

	@Builder
	public Chat(Long roomId, Long userId, String senderType, String content, LocalDateTime sentAt) {
		this.roomId = roomId;
		this.userId = userId;
		this.senderType = senderType;
		this.content = content;
		this.sentAt = sentAt;
	}






}
