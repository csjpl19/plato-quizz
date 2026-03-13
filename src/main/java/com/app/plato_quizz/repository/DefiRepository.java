package com.app.plato_quizz.repository;

import com.app.plato_quizz.model.Defi;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DefiRepository extends JpaRepository<Defi, Long> {

    @Query("select d from Defi d where d.active = true order by d.startDate asc, d.id asc")
    List<Defi> findActiveDefis();

    @EntityGraph(attributePaths = {"questions"})
    @Query("select d from Defi d where d.id = :defiId")
    Optional<Defi> findByIdWithQuestions(Long defiId);
}
