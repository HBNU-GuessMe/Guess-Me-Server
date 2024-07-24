package com.hanbat.guessmebackend.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

public class CommentQuestionRepositoryCustomImpl implements CommentQuestionRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public CommentQuestion findFirstByQuestionIdAndUserId(Long questionId, Long userId) {
		TypedQuery<CommentQuestion> query = entityManager.createQuery(
			"SELECT cq FROM CommentQuestion cq " +
				"JOIN cq.user u " +
				"JOIN cq.question q " +
				"WHERE q.id = :questionId AND u.id = :userId " +
				"AND cq.publishedAt IS NULL " +
				"ORDER BY cq.id ASC", CommentQuestion.class
		);
		query.setParameter("questionId", questionId);
		query.setParameter("userId", userId);
		query.setMaxResults(1); // 최대 1개의 결과만 가져오도록 설정

		return query.getSingleResult();
		 // 결과가 없으면 null 반환
	}
}

