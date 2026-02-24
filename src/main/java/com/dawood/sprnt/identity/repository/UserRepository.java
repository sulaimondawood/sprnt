package com.dawood.sprnt.identity.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawood.sprnt.identity.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmailIgnoreCase(String email);

  Optional<User> findByToken(String token);

  boolean existsByEmailIgnoreCase(String email);

}
