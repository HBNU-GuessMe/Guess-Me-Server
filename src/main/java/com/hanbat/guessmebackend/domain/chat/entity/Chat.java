package com.hanbat.guessmebackend.domain.chat.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import jakarta.persistence.Id;

@Document(collation = "chat")
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


}
