package com.app.plato_quizz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "defi_participation",
    uniqueConstraints = @UniqueConstraint(name = "uk_defi_participation_user_defi", columnNames = {"user_id", "defi_id"})
)
public class DefiParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "defi_participation_seq")
    @SequenceGenerator(name = "defi_participation_seq", sequenceName = "defi_participation_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserPQ user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defi_id", nullable = false)
    private Defi defi;

    @Column(name = "score_points", nullable = false)
    private int scorePoints;

    @Column(name = "correct_answers", nullable = false)
    private int correctAnswers;

    @Column(name = "total_questions", nullable = false)
    private int totalQuestions;

    @Column(name = "played_at", nullable = false)
    private LocalDateTime playedAt;

    public Long getId() {
        return id;
    }

    public UserPQ getUser() {
        return user;
    }

    public Defi getDefi() {
        return defi;
    }

    public int getScorePoints() {
        return scorePoints;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserPQ user) {
        this.user = user;
    }

    public void setDefi(Defi defi) {
        this.defi = defi;
    }

    public void setScorePoints(int scorePoints) {
        this.scorePoints = scorePoints;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }
}
