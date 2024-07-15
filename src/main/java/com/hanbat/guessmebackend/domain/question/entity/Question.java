package com.hanbat.guessmebackend.domain.question.entity;

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
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "BIGINT(11)")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "family_id", nullable = false)
	private Family family;

	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String content;

	@Column(name = "published_at", columnDefinition = "DATETIME")
	private LocalDateTime publishedAt;

	@Column(name = "updated_at", columnDefinition = "DATETIME")
	private LocalDateTime updatedAt;

	@Column(name = "is_passed", columnDefinition = "TINYINT(1)", nullable = false)
	private Boolean isPassed;

	@Builder
	private Question(Family family, String content, Boolean isPassed) {
		this.family = family;
		this.content = content;
		this.isPassed = isPassed;
	}

	public void updateIsPassed(Boolean isPassed) {
		this.isPassed = isPassed;
	}

	public void updatePublishedAt(LocalDateTime publishedAt) {
		this.publishedAt = publishedAt;
	}

	public void updateUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}


}
