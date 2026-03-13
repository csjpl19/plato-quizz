package com.app.plato_quizz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_statistic")
public class UserStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_statistic_seq")
    @SequenceGenerator(name = "user_statistic_seq", sequenceName = "user_statistic_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserPQ user;

    @Column(name = "total_defi_points", nullable = false)
    private int totalDefiPoints;

    @Column(name = "total_defis_completed", nullable = false)
    private int totalDefisCompleted;

    @Column(name = "total_defi_questions", nullable = false)
    private int totalDefiQuestions;

    @Column(name = "total_defi_correct_answers", nullable = false)
    private int totalDefiCorrectAnswers;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public UserPQ getUser() {
        return user;
    }

    public int getTotalDefiPoints() {
        return totalDefiPoints;
    }

    public int getTotalDefisCompleted() {
        return totalDefisCompleted;
    }

    public int getTotalDefiQuestions() {
        return totalDefiQuestions;
    }

    public int getTotalDefiCorrectAnswers() {
        return totalDefiCorrectAnswers;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserPQ user) {
        this.user = user;
    }

    public void setTotalDefiPoints(int totalDefiPoints) {
        this.totalDefiPoints = totalDefiPoints;
    }

    public void setTotalDefisCompleted(int totalDefisCompleted) {
        this.totalDefisCompleted = totalDefisCompleted;
    }

    public void setTotalDefiQuestions(int totalDefiQuestions) {
        this.totalDefiQuestions = totalDefiQuestions;
    }

    public void setTotalDefiCorrectAnswers(int totalDefiCorrectAnswers) {
        this.totalDefiCorrectAnswers = totalDefiCorrectAnswers;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
