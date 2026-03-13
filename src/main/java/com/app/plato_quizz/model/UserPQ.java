package com.app.plato_quizz.model;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "UserPQ")
public class UserPQ {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private int id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name ="google_id", unique = true)
    private String googleId;

    @Column(name = "password")
    private String password;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "auth_provider", nullable = false)
    private String authProvider;

    @Column(name="statut", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutPQ statut;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatistic statistic;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DefiParticipation> defiParticipations = new ArrayList<>();

    public UserPQ() {}

    public UserPQ(String email, String fullname, String googleId, String password, StatutPQ statut) {
        this(email, fullname, googleId, password, null, "MANUAL", statut);
    }

    public UserPQ(String email, String fullname, String googleId, String password, String pictureUrl, String authProvider, StatutPQ statut) {
        this.email = email;
        this.fullname = fullname;
        this.googleId = googleId;
        this.password = password;
        this.pictureUrl = pictureUrl;
        this.authProvider = authProvider;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }   

    public String getGoogleId() {
        return googleId;
    }

    public String getPassword() {
        return password;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getAuthProvider() {
        return authProvider;
    }
    
    public StatutPQ getStatut() {
        return statut;
    }

    public UserStatistic getStatistic() {
        return statistic;
    }

    public List<DefiParticipation> getDefiParticipations() {
        return defiParticipations;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setAuthProvider(String authProvider) {
        this.authProvider = authProvider;
    }
    
    public void setStatut(StatutPQ statut) {
        this.statut = statut;
    }

    public void setStatistic(UserStatistic statistic) {
        this.statistic = statistic;
    }

    public void setDefiParticipations(List<DefiParticipation> defiParticipations) {
        this.defiParticipations = defiParticipations;
    }

    @Override
    public String toString() {
        return "UserPQ [id=" + id + ", email=" + email + ", fullname=" + fullname + ", googleId=" + googleId + ", pictureUrl=" + pictureUrl + ", authProvider=" + authProvider + ", statut=" + statut + "]";
    }       
}
