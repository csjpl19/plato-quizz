package com.app.plato_quizz.repository;

import com.app.plato_quizz.model.UserPQ;
import com.app.plato_quizz.model.UserStatistic;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatisticRepository extends JpaRepository<UserStatistic, Long> {

    Optional<UserStatistic> findByUser(UserPQ user);
}
