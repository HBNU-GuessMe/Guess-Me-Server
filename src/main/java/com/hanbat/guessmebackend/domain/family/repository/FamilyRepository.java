package com.hanbat.guessmebackend.domain.family.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanbat.guessmebackend.domain.family.entity.Family;
import com.hanbat.guessmebackend.domain.user.entity.User;

public interface FamilyRepository extends JpaRepository<Family, Long> {
	@Query("SELECT u FROM User u JOIN u.family f WHERE f.id = :familyId" )
	Optional<List<User>> findUsersByFamilyId(@Param("familyId") Long familyId);

}
