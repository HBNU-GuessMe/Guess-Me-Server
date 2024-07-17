package com.hanbat.guessmebackend.domain.comment.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.hanbat.guessmebackend.domain.family.entity.Family;
import com.hanbat.guessmebackend.domain.question.entity.Question;
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
	@JoinColumn(name = "question_id", nullable = false)
	private Question question;

	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String content;

	@Column(name = "is_passed", columnDefinition = "TINYINT(1)", nullable = false)
	private Boolean isPassed;

	@Column(name = "published_at", columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime publishedAt;

	@Builder
	public CommentQuestion(User user, Question question, String content,
		Boolean isPassed) {
		this.user = user;
		this.question = question;
		this.content = content;
		this.isPassed = isPassed;
	}

	public void updateIsPassed(Boolean isPassed) {
		this.isPassed = isPassed;
	}

	public void updatePublishedAt(LocalDateTime publishedAt) {
		this.publishedAt = publishedAt;
	}

}
