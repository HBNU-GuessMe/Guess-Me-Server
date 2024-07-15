package com.hanbat.guessmebackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.hanbat.guessmebackend.domain.chat.application.ChatService;
import com.hanbat.guessmebackend.domain.chat.entity.Chat;
import com.hanbat.guessmebackend.domain.chat.entity.Chatroom;
import com.hanbat.guessmebackend.domain.chat.repository.ChatRoomRepository;
import com.hanbat.guessmebackend.domain.family.entity.Family;
import com.hanbat.guessmebackend.domain.family.repository.FamilyRepository;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("development")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChatServiceTest {

	@InjectMocks
	private ChatService chatService;

	@Mock
	private SimpMessagingTemplate simpMessagingTemplate;

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@Mock
	private FamilyRepository familyRepository;


	@Test
	public void testSendEndMessage() {
		// Mock 데이터 설정
		Family family = Family.builder()
			.familyCode("12345")
			.count(5)
			.build();

		familyRepository.save(family);

		Chatroom chatroom = Chatroom.builder()
			.family(family)
			.createdAt(LocalDateTime.now())
			.isEnded(false)
			.build();

		chatRoomRepository.save(chatroom);


		// 테스트할 메시지 내용
		String expectedMessage = "24시간이 지났으므로 채팅방이 닫혀요! 다음 속마음 채팅 때 만나요:)";

		when(chatRoomRepository.findById(chatroom.getId())).thenReturn(Optional.of(chatroom));

		// 테스트할 메서드 호출
		chatService.sendEndMessage(chatroom.getId());

		// SimpMessagingTemplate을 통해 메시지가 제대로 전송되었는지 확인
		verify(simpMessagingTemplate, times(1)).convertAndSend("/sub/channel/" + chatroom.getId(),  expectedMessage);

		// Chatroom 업데이트가 제대로 수행되었는지 확인
		verify(chatRoomRepository, times(1)).findById(chatroom.getId());
		assertTrue(chatroom.isEnded());
	}
}
