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

	// Authentication
	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 헤더에 없습니다."),
	AUTH_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "시큐리티 인증 정보를 찾을 수 없습니다."),
	TOKEN_IS_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

	// User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
	USER_SOCIAL_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "소셜 정보를 찾을 수 없습니다."),
	USER_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 가입된 유저입니다."),
	USER_CODE_NOT_MATCHED(HttpStatus.BAD_REQUEST, "코드가 일치하지않습니다."),
	// USER_ROLE_IS_NOT_WARD(HttpStatus.BAD_REQUEST, "유저가 피보호자가 아닙니다."),


	// Family
	FAMILY_CODE_CONNECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 코드를 찾을 수 없습니다."),
	FAMILY_CONNECTION_DURATION_OVERTIME(HttpStatus.BAD_REQUEST, "가족 연결 대기 시간이 초과되었습니다. 다시 시도해주세요."),
	FAMILY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 가족을 찾을 수 없습니다."),
	FAMILY_CODE_IS_NOT_OWNER(HttpStatus.BAD_REQUEST, "해당 코드의 주인이 아닙니다."),
	FAMILY_AND_USERS_NOT_FOUND(HttpStatus.BAD_REQUEST, "가족에 연결된 유저를 찾을 수 없습니다."),
	THIS_CODE_IS_YOUR_CODE(HttpStatus.BAD_REQUEST, "이 코드는 당신의 코드이므로 입력할 수 없습니다."),
	FAMILY_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 가족이 연결된 코드입니다."),

	// Question
	QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 질문을 찾을 수 없습니다."),
	QUESTION_NOT_READY(HttpStatus.BAD_REQUEST, "질문이 아직 준비되지않았습니다."),
	QUESTION_SCHEDULER_NOT_WORKING(HttpStatus.INTERNAL_SERVER_ERROR, "질문 스케줄러에서 에러가 발생했습니다."),


	// Answer
	ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 답변을 찾을 수 없습니다."),
	FINISHED_QUESTION_ANSWER_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "종료된 질문에는 답변을 할 수 없습니다."),
	NOT_PASSED_QUESTION(HttpStatus.BAD_REQUEST, "질문이 패스되지않아 다른 가족 구성원의 답변을 볼 수 없습니다."),
	NOT_COMPLETED_QUESTION_ALL(HttpStatus.BAD_REQUEST, "질문에 대한 답을 하지 않은 가족 구성원 있어 다른 사람의 답변을 볼 수 없습니다."),

	// CommentQuestion
	COMMENT_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글 질문을 찾을 수 없습니다."),
	COMMENT_QUESTION_NOT_OWNER(HttpStatus.BAD_REQUEST, "해당 댓글 질문의 소유자가 아니므로 답변할 수 없습니다."),
	COMMENT_QUESTION_NOT_PUBLISHED_YET(HttpStatus.BAD_REQUEST, "해당 댓글 질문은 아직 발행되지않아 답변할 수 없습니다."),

	// Chat
	CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String message;
}
