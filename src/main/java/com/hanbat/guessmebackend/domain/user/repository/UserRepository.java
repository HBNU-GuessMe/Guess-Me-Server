package com.hanbat.guessmebackend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbat.guessmebackend.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
