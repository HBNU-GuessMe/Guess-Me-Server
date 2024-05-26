package com.hanbat.guessmebackend.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbat.guessmebackend.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Boolean existsBySnsId(String snsId);
	User findBySnsId(String snsId);
	Optional<User> findByUserCode(String userCode);
}
