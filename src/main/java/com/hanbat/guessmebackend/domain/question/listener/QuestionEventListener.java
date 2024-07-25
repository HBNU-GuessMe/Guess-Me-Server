package com.hanbat.guessmebackend.domain.question.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.guessmebackend.domain.answer.application.AnswerService;
import com.hanbat.guessmebackend.domain.answer.dto.AnswerGetAllResponse;
import com.hanbat.guessmebackend.domain.answer.entity.Answer;
import com.hanbat.guessmebackend.domain.comment.application.CommentService;
import com.hanbat.guessmebackend.domain.question.application.ChatgptQuestionService;
import com.hanbat.guessmebackend.domain.question.entity.Question;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuestionEventListener {

	private final ChatgptQuestionService chatgptQuestionService;
	private final AnswerService answerService;
	private final CommentService commentService;
	/*
		답변할 때마다 답변 카운트 수와 가족 구성원의 수가 같은지 확인 후, 같으면 isPassed를 true로 변경
		만약, 답변 수정하면, content를 수정하므로 카운트 수 동일
	 */
	@EventListener
	@Transactional
	public void handleQuestionEvent(Answer answer) throws JsonProcessingException {
		// Question으로 파라미터받을시 안됨 (이유찾기)
	if (answer.getQuestion().getAnswerCount() >= answer.getQuestion().getFamily().getCount()){
			answer.getQuestion().updateIsPassed(true);
			commentService.registerCommentQuestion(chatgptQuestionService.responseJSONFromChatgptApi(createRequest(answer.getQuestion())));
		}
	}



	/*
		ChatGPT message param에 들어갈 request 생성
	 */
	public List<Map<String, Object>> createRequest (Question question) {
		Map<String, Object> message1 = new HashMap<>();
		message1.put("role", "system");
		message1.put("content", "당신은 가족 상담을 하기위한 전문의입니다. 예의있게 대답해주세요. 이 json 파일에서 질문에 대해 각 사용자가 대답한 내용을 바탕으로 왜 이렇게 대답했는지 등 새로운 질문을 만들어주세요. ");

		Map<String, Object> message2 = new HashMap<>();
		message2.put("role", "user");
		AnswerGetAllResponse answerGetAllResponse = answerService.getAllAnswers(question.getId());
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.valueToTree(answerGetAllResponse);
		message2.put("content", jsonNode + "이 json 파일에서 각 userId가 대답한 내용을 바탕으로 제 3자의 입장, 상담가로써 새로운 질문을 2개 만들어줘. "
			+ "반드시 각 userId마다 질문을 2개씩 생성해줘" + "rootnode는 data이고, key는 questionId와 commentQuestions가 있어.  commentQuestions 안에 id와 userId, userName, questionContent가 있어. "
			+ "{data : {questionId, commentQuestions : [{id, userId, userName, questionContent}, ..]} 다음과 같은 형식이야. questionId와 commentQuestions은 앞에서 한번만 나와. 이 JSON 형식을 맞춰줘");


		List<Map<String, Object>> messages = new ArrayList<>();
		messages.add(message1);
		messages.add(message2);

		log.info(messages.toString());
		return messages;
	}


}
