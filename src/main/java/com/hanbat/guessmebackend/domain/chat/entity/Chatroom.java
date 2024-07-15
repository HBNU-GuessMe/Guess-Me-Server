package com.hanbat.guessmebackend.domain.chat.entity;

import java.time.LocalDateTime;

import com.hanbat.guessmebackend.domain.common.model.BaseTimeEntity;
import com.hanbat.guessmebackend.domain.family.entity.Family;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "BIGINT(11)")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "family_id", nullable = false)
	private Family family;

	@Column(name = "created_at", columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "is_ended", columnDefinition = "TINYINT(1)", nullable = false)
	private boolean isEnded;

	@Builder
	public Chatroom(Family family, LocalDateTime createdAt, boolean isEnded) {
		this.family = family;
		this.createdAt = createdAt;
		this.isEnded = isEnded;
	}

	public void updateIsEnded(boolean isEnded) {
		this.isEnded = isEnded;
	}


}
