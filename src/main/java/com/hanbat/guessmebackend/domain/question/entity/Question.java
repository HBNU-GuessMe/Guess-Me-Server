package com.hanbat.guessmebackend.domain.question.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
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

	@Column(name = "is_passed", columnDefinition = "TINYINT(1)", nullable = false)
	private Boolean isPassed;

	@Builder.Default
	@Column(name = "answer_count", columnDefinition = "INT(5)", nullable = false)
	private int answerCount = 0;

	@Builder.Default
	@Column(name = "comment_question_count", columnDefinition = "INT(5)", nullable = false)
	private int commentQuestionCount = 2;


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

	public void updateAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	public void updateCommentQuestionCount(int commentQuestionCount) {
		this.commentQuestionCount = commentQuestionCount;
	}

}
