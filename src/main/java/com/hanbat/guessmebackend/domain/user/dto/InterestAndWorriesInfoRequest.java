package com.hanbat.guessmebackend.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class InterestAndWorriesInfoRequest {
	@NotNull private String interest;
	@NotNull private String worry;
}
