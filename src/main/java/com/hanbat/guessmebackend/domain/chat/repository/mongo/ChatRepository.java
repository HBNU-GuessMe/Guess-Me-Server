package com.hanbat.guessmebackend.domain.chat.repository.mongo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hanbat.guessmebackend.domain.chat.entity.Chat;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
	/* 스크롤 페이징 처리 조회 */
	Slice<Chat> findByRoomIdOrderBySentAtAsc(Long roomId, Pageable pageable);
}
