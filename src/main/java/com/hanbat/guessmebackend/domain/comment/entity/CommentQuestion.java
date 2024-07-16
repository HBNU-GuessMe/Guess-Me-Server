package com.hanbat.guessmebackend.domain.comment.entity;

import java.time.LocalDateTime;

import com.hanbat.guessmebackend.domain.family.entity.Family;
import com.hanbat.guessmebackend.domain.user.entity.User;

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
public class CommentQuestion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "BIGINT(11)")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "family_question_id", nullable = false)
	private FamilyCommentQuestion familyCommentQuestion;

	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String content;

	@Column(name = "is_passed", columnDefinition = "TINYINT(1)", nullable = false)
	private Boolean isPassed;

	@Column(name = "published_at", columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime publishedAt;

	@Column(name = "updated_at", columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime updatedAt;

	@Builder
	public CommentQuestion(User user, FamilyCommentQuestion familyCommentQuestion, String content,
		Boolean isPassed) {
		this.user = user;
		this.familyCommentQuestion = familyCommentQuestion;
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
