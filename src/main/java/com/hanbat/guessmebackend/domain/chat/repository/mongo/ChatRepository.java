package com.hanbat.guessmebackend.domain.chat.repository.mongo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hanbat.guessmebackend.domain.chat.entity.Chat;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {

	List<Chat> findChatsByRoomId(Long chatroomId, Sort sort);
}
