package com.hanbat.guessmebackend.domain.family.entity;


import com.hanbat.guessmebackend.domain.common.model.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Family extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "BIGINT(11)")
	private Long id;

	@Column(name = "family_code", columnDefinition = "VARCHAR(10)", nullable = false)
	private String familyCode;

	@Column(columnDefinition = "INT(5)", nullable = false)
	private int count;

	@Builder
	private Family(String familyCode, int count) {
		this.familyCode = familyCode;
		this.count = count;
	}
}
