package com.app.plato_quizz.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "defi")
public class Defi {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "defi_seq")
    @SequenceGenerator(name = "defi_seq", sequenceName = "defi_seq", allocationSize = 1)
    private Long id;

    @Column(name = "titre", nullable = false, length = 140)
    private String titre;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "defi", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("ordreQuestion asc")
    private List<DefiQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "defi", fetch = FetchType.LAZY)
    private List<DefiParticipation> participations = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return active;
    }

    public List<DefiQuestion> getQuestions() {
        return questions;
    }

    public List<DefiParticipation> getParticipations() {
        return participations;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setQuestions(List<DefiQuestion> questions) {
        this.questions = questions;
    }

    public void setParticipations(List<DefiParticipation> participations) {
        this.participations = participations;
    }
}
