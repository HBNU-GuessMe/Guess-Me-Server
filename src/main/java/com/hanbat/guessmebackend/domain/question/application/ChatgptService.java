package com.hanbat.guessmebackend.domain.question.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.guessmebackend.domain.question.dto.QuestionCreateResponse;
import com.hanbat.guessmebackend.domain.question.entity.Question;
import com.hanbat.guessmebackend.domain.question.repository.QuestionRepository;
import com.hanbat.guessmebackend.domain.user.entity.Role;
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
public class ChatgptService {

	@Value("${openai.chatgpt.api-key}")
	private String apiKey;

	private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";

	private final MemberUtil memberUtil;

	private final QuestionRepository questionRepository;

	// openAI dependency 사용 X

	/*
		처음 생성) '질문 생성하러가기' 버튼누르면 자녀들의 관심사, 고민거리 스트링으로 넘겨주면 관련 질문 5개 생성
		후 생성) 답변할 때마다 프론트에서 질문 isDone 상태가 되면 다시 이 API 요청

	 */

	@Transactional
	public List<QuestionCreateResponse> createQuestion() throws JsonProcessingException {
		final User user = memberUtil.getCurrentUser();
		if (user.getRole() != Role.WARD) {
			throw new CustomException(ErrorCode.USER_ROLE_IS_NOT_WARD);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization","Bearer " + apiKey);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("model", "ft:gpt-3.5-turbo-1106:personal:aigueseme:9kl9IecO");
		List<Map<String, Object>> messages = createRequest(user);

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

		// 원하는 형태로 파싱
		String[] lines = content.split("\\n");
		List<QuestionCreateResponse> questions = new ArrayList<>();
		Question question = null;
		for (int i=0;i<5;i++) {
			int index = lines[i].indexOf('.');
			if (index == -1) {
				question = Question.builder()
					.content(lines[i])
					.isDone(false)
					.family(user.getFamily())
					.build();
			} else {
				String contentLine = lines[i].substring(lines[i].indexOf('.') + 2);
				log.info(contentLine);
				question = Question.builder()
					.content(contentLine)
					.isDone(false)
					.family(user.getFamily())
					.build();
			}

			questionRepository.save(question);
			questions.add(QuestionCreateResponse.fromQuestion(question));

		}

		return questions;

	}

	/*
		ChatGPT message param에 들어갈 request 생성
	 */
	private List<Map<String, Object>> createRequest (User user) {
		Map<String, Object> message1 = new HashMap<>();
		message1.put("role", "system");
		message1.put("content", "당신은 가족입니다.");

		Map<String, Object> message2 = new HashMap<>();
		message2.put("role", "user");
		message2.put("content", user.getNickname() + "는 " + user.getInterest() + "에 관심을 가지고 있어. 그리고, "
			+ user.getWorry() + "(이) 현재 걱정거리야. " + user.getNickname() +
			"에게 주어진 관심사와 걱정거리를 바탕으로 각 질문마다 이름을 포함하고, 번호를 포함해 질문을 구체적으로 5개 만들어줘.");

		List<Map<String, Object>> messages = new ArrayList<>();
		messages.add(message1);
		messages.add(message2);

		log.info(messages.toString());
		return messages;
	}

	// 처음 생성) '질문받으러가기' 버튼누르면 자녀들의 관심사, 고민거리 스트링으로 넘겨주면 관련 질문 20개 생성
	// 조회) 질문을 만든 시점으로부터 24시간이 딱 지났을때 질문 질문이 없는지 있는지 예외처리

	// 다시 생성) 24시간이 다 지나거나 가족이 다 답변을 완료했을 때 isDone을 true로 하면, 프론트엔드에게 어떻게?
	// 답변을 chatgpt에게 전달해서 궁금한 질문 3개 다시 생성
}
