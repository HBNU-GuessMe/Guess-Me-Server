package com.hanbat.guessmebackend.domain.question.entity;

import java.time.LocalDateTime;

import com.hanbat.guessmebackend.domain.common.model.BaseTimeEntity;
import com.hanbat.guessmebackend.domain.family.entity.Family;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
	@Column(columnDefinition = "BIGINT(11)")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "family_id", nullable = false)
	private Family family;

	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String content;

	@Column(name = "publish_date", columnDefinition = "DATETIME")
	private LocalDateTime publishDate;

	@Column(name = "is_done", columnDefinition = "TINYINT(1)", nullable = false)
	private Boolean isDone;

	@Builder(access = AccessLevel.PRIVATE)
	private Question(
		Family family,
		String content,
		Boolean isDone
	) {
		this.family = family;
		this.content = content;
		this.isDone = isDone;
	}

	public void updateIsDone(Boolean isDone) {
		this.isDone = isDone;
	}

	public void updatePublishDate(LocalDateTime publishDate) {
		this.publishDate = publishDate;
	}



}
