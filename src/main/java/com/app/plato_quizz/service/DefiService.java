package com.app.plato_quizz.service;

import com.app.plato_quizz.model.Defi;
import com.app.plato_quizz.model.DefiParticipation;
import com.app.plato_quizz.model.DefiQuestion;
import com.app.plato_quizz.model.UserPQ;
import com.app.plato_quizz.model.UserStatistic;
import com.app.plato_quizz.repository.DefiParticipationRepository;
import com.app.plato_quizz.repository.DefiRepository;
import com.app.plato_quizz.repository.UserStatisticRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefiService {

    private final DefiRepository defiRepository;
    private final DefiParticipationRepository defiParticipationRepository;
    private final UserStatisticRepository userStatisticRepository;

    public DefiService(
        DefiRepository defiRepository,
        DefiParticipationRepository defiParticipationRepository,
        UserStatisticRepository userStatisticRepository
    ) {
        this.defiRepository = defiRepository;
        this.defiParticipationRepository = defiParticipationRepository;
        this.userStatisticRepository = userStatisticRepository;
    }

    @Transactional(readOnly = true)
    public List<DefiListItem> listAvailableDefisForUser(UserPQ user) {
        LocalDate today = LocalDate.now();
        List<Defi> allActiveDefis = defiRepository.findActiveDefis();
        List<DefiParticipation> participations = defiParticipationRepository.findByUser(user);
        Map<Long, DefiParticipation> participationByDefiId = new LinkedHashMap<>();
        for (DefiParticipation participation : participations) {
            participationByDefiId.put(participation.getDefi().getId(), participation);
        }

        List<DefiListItem> items = new ArrayList<>();
        for (Defi defi : allActiveDefis) {
            if (!isDefiAvailable(defi, today)) {
                continue;
            }
            items.add(new DefiListItem(defi, participationByDefiId.get(defi.getId())));
        }
        return items;
    }

    @Transactional(readOnly = true)
    public Optional<Defi> findPlayableDefi(Long defiId) {
        LocalDate today = LocalDate.now();
        Optional<Defi> defiOptional = defiRepository.findByIdWithQuestions(defiId);
        if (defiOptional.isEmpty()) {
            return Optional.empty();
        }

        Defi defi = defiOptional.get();
        if (!isDefiAvailable(defi, today)) {
            return Optional.empty();
        }

        defi.getQuestions().sort(Comparator.comparingInt(DefiQuestion::getOrdreQuestion));
        return Optional.of(defi);
    }

    @Transactional(readOnly = true)
    public Optional<DefiParticipation> findParticipation(UserPQ user, Defi defi) {
        return defiParticipationRepository.findByUserAndDefi(user, defi);
    }

    @Transactional(readOnly = true)
    public Optional<UserStatistic> findStatistic(UserPQ user) {
        return userStatisticRepository.findByUser(user);
    }

    @Transactional
    public DefiSubmissionResult submitDefiAnswers(Long defiId, UserPQ user, Map<String, String> rawAnswers) {
        Defi defi = findPlayableDefi(defiId)
            .orElseThrow(() -> new IllegalArgumentException("Défi introuvable ou indisponible"));
        if (defi.getQuestions().isEmpty()) {
            throw new IllegalArgumentException("Ce défi ne contient aucune question");
        }

        int score = 0;
        int correctAnswers = 0;
        int totalQuestions = defi.getQuestions().size();

        for (DefiQuestion question : defi.getQuestions()) {
            String answerKey = "q_" + question.getId();
            String selectedOption = normalizeOption(rawAnswers.get(answerKey));
            String expectedOption = normalizeOption(question.getCorrectOption());
            if (selectedOption != null && selectedOption.equals(expectedOption)) {
                correctAnswers++;
                score += Math.max(question.getPoints(), 0);
            }
        }

        DefiParticipation participation = defiParticipationRepository.findByUserAndDefi(user, defi).orElseGet(() -> {
            DefiParticipation newParticipation = new DefiParticipation();
            newParticipation.setUser(user);
            newParticipation.setDefi(defi);
            return newParticipation;
        });

        boolean alreadyCompleted = participation.getId() != null;
        int previousScore = participation.getScorePoints();
        int previousCorrectAnswers = participation.getCorrectAnswers();
        int previousTotalQuestions = participation.getTotalQuestions();

        participation.setScorePoints(score);
        participation.setCorrectAnswers(correctAnswers);
        participation.setTotalQuestions(totalQuestions);
        participation.setPlayedAt(LocalDateTime.now());
        defiParticipationRepository.save(participation);

        UserStatistic statistic = userStatisticRepository.findByUser(user).orElseGet(() -> {
            UserStatistic created = new UserStatistic();
            created.setUser(user);
            created.setTotalDefiPoints(0);
            created.setTotalDefisCompleted(0);
            created.setTotalDefiQuestions(0);
            created.setTotalDefiCorrectAnswers(0);
            return created;
        });

        if (alreadyCompleted) {
            statistic.setTotalDefiPoints(statistic.getTotalDefiPoints() + (score - previousScore));
            statistic.setTotalDefiQuestions(statistic.getTotalDefiQuestions() + (totalQuestions - previousTotalQuestions));
            statistic.setTotalDefiCorrectAnswers(statistic.getTotalDefiCorrectAnswers() + (correctAnswers - previousCorrectAnswers));
        } else {
            statistic.setTotalDefisCompleted(statistic.getTotalDefisCompleted() + 1);
            statistic.setTotalDefiPoints(statistic.getTotalDefiPoints() + score);
            statistic.setTotalDefiQuestions(statistic.getTotalDefiQuestions() + totalQuestions);
            statistic.setTotalDefiCorrectAnswers(statistic.getTotalDefiCorrectAnswers() + correctAnswers);
        }
        statistic.setUpdatedAt(LocalDateTime.now());
        userStatisticRepository.save(statistic);

        return new DefiSubmissionResult(defi, score, correctAnswers, totalQuestions, alreadyCompleted, statistic.getTotalDefiPoints());
    }

    private boolean isDefiAvailable(Defi defi, LocalDate today) {
        if (!defi.isActive()) {
            return false;
        }
        if (defi.getStartDate() != null && defi.getStartDate().isAfter(today)) {
            return false;
        }
        return defi.getEndDate() == null || !defi.getEndDate().isBefore(today);
    }

    private String normalizeOption(String option) {
        if (option == null) {
            return null;
        }
        String trimmed = option.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed.substring(0, 1).toUpperCase();
    }

    public record DefiListItem(Defi defi, DefiParticipation participation) {
    }

    public record DefiSubmissionResult(
        Defi defi,
        int score,
        int correctAnswers,
        int totalQuestions,
        boolean alreadyCompleted,
        int userTotalDefiPoints
    ) {
    }
}
