package com.hanbat.guessmebackend.domain.chat.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.guessmebackend.domain.chat.dto.ChatResponse;
import com.hanbat.guessmebackend.domain.chat.dto.Message;
import com.hanbat.guessmebackend.domain.chat.entity.Chat;
import com.hanbat.guessmebackend.domain.chat.entity.Chatroom;
import com.hanbat.guessmebackend.domain.chat.repository.ChatRoomRepository;
import com.hanbat.guessmebackend.domain.chat.repository.mongo.ChatRepository;
import com.hanbat.guessmebackend.domain.user.entity.User;
import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;
import com.hanbat.guessmebackend.global.jwt.MemberUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

	@Value("${openai.chatgpt.api-key}")
	private String apiKey;

	private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";

	private final ChatRepository chatRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final MemberUtil memberUtil;
	private final SimpMessagingTemplate simpMessagingTemplate;

	/*
		채팅을 mongodb에 저장
	 */


	/*
		채팅방 클릭 시, 사용자가 읽지않은 것부터 조회하고, 읽음처리

	 */

	/*
		채팅방에 사용자가 접속 중인지 확인 (readCount = 0)
	 */

	/*
		채팅방에 사용자가 접속 중이 아니라면 (readCount = 1)
	 */

	/*
		해피가 채팅방에 메세지 전송
	 */
	@Transactional
	public String sendMessageByServer(Long roomId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization","Bearer " + apiKey);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("model", "ft:gpt-3.5-turbo-1106:personal:aigueseme:9kl9IecO");
		List<Map<String, Object>> messages = createRequest();

		requestBody.put("messages", messages);
		requestBody.put("temperature", 0.6);
		requestBody.put("max_tokens", 700);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, request, Map.class);

		// response 파싱
		Map<String, Object> body = response.getBody();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.convertValue(body, JsonNode.class);
		log.info(node.toString());
		JsonNode message = node.get("choices").get(0).get("message");
		String content = message.get("content").asText();
		log.info(content);

		// chat에 저장
		Chat chat = Chat.builder()
			.roomId(roomId)
			.userId(null)
			.senderType("서버")
			.content(content)
			.sentAt(LocalDateTime.now())
			.build();

		chatRepository.save(chat);

		return content;
	}

	/*
		채팅방 생성 후 24시간이 지나면 종료되었다는 채팅 메세지 보내기
		- 클라이언트는 메세지받고 속마음 채팅 버튼 비활성화
	 */
	@Transactional
	public void sendEndMessage(Long roomId) {
		log.info("메세지 보내기");
		String content = "24시간이 지났으므로 채팅방이 닫혀요! 다음 속마음 채팅 때 만나요:)";
		simpMessagingTemplate.convertAndSend(
			"/sub/channel/" + roomId, content
		);
		Chatroom chatroom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
		log.info("채팅 찾음");
		chatroom.updateIsEnded(true);
		log.info("업데이트 함");
	}

	private List<Map<String, Object>> createRequest () {
		Map<String, Object> message1 = new HashMap<>();
		message1.put("role", "system");
		message1.put("content", "당신은 가족 상담을 하기위한 전문의입니다.");

		Map<String, Object> message2 = new HashMap<>();
		message2.put("role", "user");
		message2.put("content", "수현이는 예술을 하고싶고, 꿈을 이루고싶어해. 부모님은 그 꿈이 불안정하다고 생각해. 가족상담을 위한 전문의로써 위 내용을 바탕으로 상담을 이어갈 질문 1개를 구체적으로 생성해줘.");

		List<Map<String, Object>> messages = new ArrayList<>();
		messages.add(message1);
		messages.add(message2);

		log.info(messages.toString());
		return messages;
	}










}
