package com.app.plato_quizz.repository;

import com.app.plato_quizz.model.Defi;
import com.app.plato_quizz.model.DefiParticipation;
import com.app.plato_quizz.model.UserPQ;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefiParticipationRepository extends JpaRepository<DefiParticipation, Long> {

    Optional<DefiParticipation> findByUserAndDefi(UserPQ user, Defi defi);

    List<DefiParticipation> findByUser(UserPQ user);
}
