package com.hanbat.guessmebackend.domain.chat.application;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.chat.entity.Chatroom;
import com.hanbat.guessmebackend.domain.chat.repository.ChatRoomRepository;
import com.hanbat.guessmebackend.domain.family.entity.Family;
import com.hanbat.guessmebackend.domain.family.repository.FamilyRepository;
import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final FamilyRepository familyRepository;
	private final ApplicationEventPublisher eventPublisher;

	/*
		채팅방 생성
		24시간이 지나면 클라이언트에서 채팅방 버튼 숨기기
	 */
	@Transactional
	public Chatroom createChatRoom(Long familyId) {

		log.info("채팅방 생성");
		Family family = familyRepository.findById(familyId)
			.orElseThrow(() -> new CustomException(ErrorCode.FAMILY_NOT_FOUND));

		Chatroom chatroom = Chatroom.builder()
			.family(family)
			.createdAt(LocalDateTime.now())
			.isEnded(false)
			.build();

		chatRoomRepository.save(chatroom);
		eventPublisher.publishEvent(chatroom);


		return chatroom;
	}






}
