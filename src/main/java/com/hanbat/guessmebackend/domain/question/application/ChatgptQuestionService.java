package com.hanbat.guessmebackend.domain.question.application;

import java.util.ArrayList;
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
public class ChatgptQuestionService {

	@Value("${openai.chatgpt.api-key}")
	private String apiKey;

	private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";

	private final MemberUtil memberUtil;

	private final QuestionRepository questionRepository;

	// openAI dependency 사용 X

	/*
		처음 생성) '질문 생성하러가기' 버튼누르면 가족 구성원들의 관심사, 고민거리 스트링으로 넘겨주면 관련 질문 5개 생성
		후 생성) 질문 isPassed 상태가 되면 다시 이 API 요청 [추후 개발]
	*/

	/*
		chatgpt api에 요청받은 response로 질문 생성
	 */
	@Transactional
	public List<QuestionCreateResponse> createQuestion() throws JsonProcessingException {
		final User user = memberUtil.getCurrentUser();
		JsonNode rootNode = responseJSONFromChatgptApi(createRequest());
		JsonNode jsonQuestions = rootNode.get("questions");
		log.info(jsonQuestions.toString());

		List<QuestionCreateResponse> questions = new ArrayList<>();
		for (JsonNode jsonNode : jsonQuestions) {
			log.info(jsonNode.toString());
			String questionContent = jsonNode.get("questionContent").asText();
			log.info(questionContent);
			Question question = Question.builder()
				.content(questionContent)
				.isPassed(false)
				.family(user.getFamily())
				.build();

			questionRepository.save(question);
			questions.add(QuestionCreateResponse.fromQuestion(question));
		}

		return questions;
	}

	/*
		chatgptAPI 요청 : Response JSON
	 */
	public JsonNode responseJSONFromChatgptApi(List<Map<String, Object>> requestMessages) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization","Bearer " + apiKey);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("model", "ft:gpt-3.5-turbo-1106:personal:familyai:9logQYga");
		List<Map<String, Object>> messages = requestMessages;

		requestBody.put("messages", messages);
		requestBody.put("temperature", 0.5);
		requestBody.put("max_tokens", 700);

		Map<String, Object> responseFormat = new HashMap<>();
		responseFormat.put("type", "json_object");
		requestBody.put("response_format", responseFormat);


		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, request, Map.class);

		// response 파싱
		Map<String, Object> body = response.getBody();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.convertValue(body, JsonNode.class);
		log.info(node.toString());
		JsonNode message = node.get("choices").get(0).get("message");
		log.info(message.toString());
		String content = message.get("content").asText();
		JsonNode result = mapper.readTree(content);
		log.info(result.toString());
		return result;

	}

	public String responseStringFromChatgptApi(List<Map<String, Object>> requestMessages) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization","Bearer " + apiKey);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("model", "ft:gpt-3.5-turbo-1106:personal:familyai:9logQYga");
		List<Map<String, Object>> messages = requestMessages;

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
		return content;
	}


	/*
		ChatGPT message param에 들어갈 request 생성: JSON
	 */
	public List<Map<String, Object>> createRequest () {
		final User user = memberUtil.getCurrentUser();

		Map<String, Object> message1 = new HashMap<>();
		message1.put("role", "system");
		message1.put("content", "당신은 가족 상담을 하기위한 전문의입니다.");

		Map<String, Object> message2 = new HashMap<>();
		message2.put("role", "user");
		message2.put("content", user.getNickname() + "는 " + user.getInterest() + "에 관심을 가지고 있어. 그리고, "
			+ user.getWorry() + "(이) 현재 걱정거리야. " + user.getNickname() +
			"에게 주어진 관심사와 걱정거리를 바탕으로 상담 질문을 5개 만들어줘."
			+ "이때 출력형식은 반드시 JSON 문자열이 아니라 형식 맞춰서 출력해줘. rootnode는 questions이고, key는 id와 questionContent야. ");

		List<Map<String, Object>> messages = new ArrayList<>();
		messages.add(message1);
		messages.add(message2);

		log.info(messages.toString());
		return messages;
	}




}
