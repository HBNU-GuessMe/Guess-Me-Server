package com.hanbat.guessmebackend.domain.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentAnswer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_answer_id", columnDefinition = "BIGINT(11)")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "commentAnswer")
	private CommentQuestion commentQuestion;

	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String content;

	public CommentAnswer(String content) {
		this.content = content;
	}

	public void updateCommentQuestion(CommentQuestion commentQuestion) {
		this.commentQuestion = commentQuestion;
	}
}
