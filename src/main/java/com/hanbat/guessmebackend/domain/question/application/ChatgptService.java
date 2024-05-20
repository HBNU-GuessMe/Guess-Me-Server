package com.hanbat.guessmebackend.domain.question.application;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatgptService {

	@Value("${openai.chatgpt.api-key}")
	private String apiKey;

	private static final String ENDPOINT = "https://api.openai.com/v1/completions";

	public String generateText(String prompt, float temperature, int maxTokens ) { // 질문할 문장, 얼마나 창의적인 답을 작성하도록 할지 정하는 값, 응답의 컨텍스트 길이
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization","Bearer " + apiKey);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("model", "ft:gpt-3.5-turbo-1106:personal:gptassist:9P8djPh7");
		requestBody.put("prompt", prompt);
		requestBody.put("temperature", temperature);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, request, Map.class);
		return response.toString();
	}

	// 처음 생성) 자녀가 관심사, 고민거리를 입력했을 때 버튼누르면 생성중 관심사, 고민거리 스트링으로 넘겨주면 관련 질문 20개 생성
	// 조회) 질문을 만든 시점으로부터 24시간이 딱 지났을때 질문이 없는지 있는지 예외처리
	// 다시 생성) 24시간이 딱 지나거나 가족이 다 답변을 완료했을 때 답변을 chatgpt에게 전달해서 궁금한 질문 3개 다시 생성
	// @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
}
