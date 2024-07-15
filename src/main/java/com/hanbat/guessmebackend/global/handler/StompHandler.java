package com.hanbat.guessmebackend.global.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;
import com.hanbat.guessmebackend.global.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 더 높은 우선순위 가지도록
public class StompHandler implements ChannelInterceptor {
	private final JwtUtil jwtUtil;

	// websocket을 통해 들어온 요청이 처리되기전 실행

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		// websocket 연결시 헤더의 jwt token 유효성 검증
		if (StompCommand.CONNECT == accessor.getCommand()) {
			log.info("StompHandler 실행");
			String token = accessor.getFirstNativeHeader("Authorization").split(" ")[1];
			if (token == null) {
				throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
			}
			if (jwtUtil.isExpired(token)) {
				throw new CustomException(ErrorCode.TOKEN_IS_EXPIRED);
			}
		}
		return message;
	}
}
