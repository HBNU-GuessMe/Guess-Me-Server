package com.hanbat.guessmebackend.global.error.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	SAMPLE_ERROR(HttpStatus.BAD_REQUEST, "Sample Error Message"),

	// Common
	METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청한 값 타입이 잘못되어 binding에 실패하였습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지않는 HTTP method입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류, 관리자에게 문의하세요."),

	// User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
	USER_SOCIAL_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "소셜 정보를 찾을 수 없습니다."),
	USER_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 가입된 유저입니다."),


	// Family
	FAMILY_CODE_CONNECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 코드를 찾을 수 없습니다."),
	FAMILY_CONNECTION_DURATION_OVERTIME(HttpStatus.BAD_REQUEST, "가족 연결 대기 시간이 초과되었습니다. 다시 시도해주세요."),
	FAMILY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 가족을 찾을 수 없습니다."),

	// Question
	QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 질문을 찾을 수 없습니다."),
	QUESTION_NOT_READY(HttpStatus.BAD_REQUEST, "질문이 아직 준비되지않았습니다."),


	// Answer
	ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 답변을 찾을 수 없습니다."),
	FINISHED_QUESTION_ANSWER_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "종료된 질문에는 답변을 할 수 없습니다."),
	NOT_COMPLETED_QUESTION_ALL(HttpStatus.BAD_REQUEST, "질문에 대한 답을 하지 않은 가족 구성원 있어 다른 사람의 답변을 볼 수 없습니다."),


	// Chat
	CHAT_ROOM_NOT_CREATED_YET(HttpStatus.BAD_REQUEST, "아직 채팅방이 생성되지 않았습니다.");

	private final HttpStatus status;
	private final String message;
}
