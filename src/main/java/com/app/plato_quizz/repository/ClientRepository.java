package com.app.plato_quizz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.plato_quizz.model.UserPQ;

@Repository
public interface ClientRepository extends JpaRepository<UserPQ, Integer> {

    Optional<UserPQ> findByEmail(String email);
    Optional<UserPQ> findByEmailIgnoreCase(String email);
    Optional<UserPQ> findByGoogleId(String googleId);
    long countByEmail(String email);
    long countByEmailIgnoreCase(String email);
    long countByGoogleId(String googleId);
}
