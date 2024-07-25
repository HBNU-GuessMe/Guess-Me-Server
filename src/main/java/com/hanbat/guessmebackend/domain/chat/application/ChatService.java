package com.hanbat.guessmebackend.domain.chat.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.guessmebackend.domain.chat.dto.ChatResponse;
import com.hanbat.guessmebackend.domain.chat.dto.Message;
import com.hanbat.guessmebackend.domain.chat.entity.Chat;
import com.hanbat.guessmebackend.domain.chat.entity.Chatroom;
import com.hanbat.guessmebackend.domain.chat.repository.ChatRoomRepository;
import com.hanbat.guessmebackend.domain.chat.repository.mongo.ChatRepository;
import com.hanbat.guessmebackend.domain.comment.application.CommentService;
import com.hanbat.guessmebackend.domain.comment.dto.CommentsByFamilyGetResponse;
import com.hanbat.guessmebackend.domain.question.application.ChatgptQuestionService;
import com.hanbat.guessmebackend.domain.user.entity.User;
import com.hanbat.guessmebackend.domain.user.repository.UserRepository;
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
	private final UserRepository userRepository;
	private final MemberUtil memberUtil;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ChatgptQuestionService chatgptQuestionService;
	private final CommentService commentService;


	/*
		채팅방 메세지 저장하기
	 */
	@Transactional
	public void createChat(Message message) {
		Chat chat = Chat.builder()
			.userId(message.getSenderId())
			.roomId(message.getRoomId())
			.content(message.getContent())
			.sentAt(LocalDateTime.now())
			.senderType("사용자")
			.build();

		chatRepository.save(chat);
	}

	public List<ChatResponse> getChats(Long chatroomId) {
		Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
		List<Chat> chats = chatRepository.findChatsByRoomId(chatroomId, sort);
		List<ChatResponse> chatResponses = new ArrayList<>();
		for (Chat chat : chats) {
			Optional<User> userOptional = userRepository.findById(chat.getUserId());

			// User 객체가 존재하는 경우 닉네임을 가져오고, 그렇지 않은 경우 null을 설정
			String nickname = userOptional.map(User::getNickname).orElse(null);

			chatResponses.add(ChatResponse.fromChat(chat, nickname));
		}
		return chatResponses;
	}

	/*
		해피가 채팅방에 메세지 전송
	 */
	@Transactional
	public String sendMessageByServer(Long roomId) throws JsonProcessingException {
		String content = chatgptQuestionService.responseStringFromChatgptApi(createRequest());
		log.info(content);

		// chat에 저장
		Chat chat = Chat.builder()
			.roomId(roomId)
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
		log.info("채팅방 찾음");
		chatroom.updateIsEnded(true);
		log.info("업데이트 함");
	}

	private List<Map<String, Object>> createRequest () {
		final User user = memberUtil.getCurrentUser();
		Long familyId = user.getFamily().getId();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> message1 = new HashMap<>();
		message1.put("role", "system");
		message1.put("content", "당신은 가족 상담을 하기위한 상담가입니다. 말투는 부드럽게, 존댓말로 해주세요.");

		Map<String, Object> message2 = new HashMap<>();
		message2.put("role", "user");
		List<CommentsByFamilyGetResponse> responses = commentService.getAllCommentsByFamily(familyId);
		JsonNode jsonResponses = mapper.convertValue(responses, JsonNode.class);
		message2.put("content", jsonResponses + "가족상담을 위한 전문의로써 위 json 파일을 바탕으로 길게 구체적으로 상담할 질문을 생성해주세요.");

		List<Map<String, Object>> messages = new ArrayList<>();
		messages.add(message1);
		messages.add(message2);

		log.info(messages.toString());
		return messages;
	}










}
