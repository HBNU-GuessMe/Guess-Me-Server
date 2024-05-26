package com.hanbat.guessmebackend.domain.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;

import com.hanbat.guessmebackend.domain.common.model.BaseTimeEntity;
import com.hanbat.guessmebackend.domain.family.entity.Family;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "BIGINT(11)")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "family_id")
	private Family family;

	@Column(name = "sns_id", columnDefinition = "VARCHAR(10)", nullable = false)
	private String snsId;

	@Enumerated(EnumType.STRING)
	@Column(name = "sns_type", nullable = false)
	private SnsType snsType;

	@Column(name = "user_code", columnDefinition = "VARCHAR(10)")
	private String userCode;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(columnDefinition = "VARCHAR(10)")
	private String nickname;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(columnDefinition = "DATETIME")
	private LocalDate birth;

	@Column(columnDefinition = "TEXT")
	private String interest;

	@Column(columnDefinition = "TEXT")
	private String worry;

	public User (String snsId, SnsType snsType) {
		this.snsId = snsId;
		this.snsType = snsType;
		createCode();
	}

	public void updateUserInfo(Role role, String nickname, Gender gender, LocalDate birth) {
		this.role = role;
		this.nickname = nickname;
		this.gender = gender;
		this.birth = birth;
	}

	// 랜덤 문자열을 만드는 작업을 서비스에서 할 필요가 없음 -> Entity 내에 만들거나 따로 클래스를 만들어줘야함
	private void createCode() {
		String userCode = RandomStringUtils.randomAlphanumeric(10);
		this.userCode = userCode;
	}

	public void updateFamily(Family family) {
		this.family = family;
	}






}
