package com.hanbat.guessmebackend.domain.family.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbat.guessmebackend.domain.family.entity.Family;

public interface FamilyRepository extends JpaRepository<Family, Long> {

}
