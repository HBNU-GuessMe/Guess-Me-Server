package com.hanbat.guessmebackend.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbat.guessmebackend.domain.chat.entity.Chatroom;

public interface ChatRoomRepository extends JpaRepository<Chatroom, Long> {

}
