package com.hanbat.guessmebackend.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanbat.guessmebackend.domain.comment.entity.CommentQuestion;

public interface CommentQuestionRepository extends JpaRepository<CommentQuestion, Long>,  CommentQuestionRepositoryCustom {

	@Query("SELECT SUM(cq.commentAnswerCount) FROM CommentQuestion cq " +
		"JOIN cq.question q " +
		"WHERE q.id = :questionId")
	Integer findSumOfCommentAnswerCount(@Param("questionId") Long questionId);

	@Query("SELECT cq, ca FROM CommentQuestion cq " +
		"LEFT JOIN cq.commentAnswer ca " +
		"JOIN cq.question q " +
		"JOIN q.family f " +
		"WHERE f.id = :familyId")
	List<Object[]> findCommentQuestionsAndAnswersByFamilyId(@Param("familyId") Long familyId);

	@Query("SELECT cq FROM CommentQuestion cq " +
		"JOIN cq.question q " +
		"WHERE q.id = :questionId AND cq.publishedAt IS NOT NULL AND cq.isPassed IS FALSE ")
	List<CommentQuestion> findCommentQuestionAlreadyPublished(@Param("questionId") Long questionId);


}
