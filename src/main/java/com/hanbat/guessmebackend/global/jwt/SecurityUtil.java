package com.hanbat.guessmebackend.global.jwt;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hanbat.guessmebackend.global.error.exception.CustomException;
import com.hanbat.guessmebackend.global.error.exception.ErrorCode;

// 토큰에서 userID 가져오기
@Component
public class SecurityUtil {
	public Long getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			return Long.parseLong(authentication.getName());
		} catch (Exception e) {
			throw new CustomException(ErrorCode.AUTH_NOT_FOUND);
		}
	}
}
