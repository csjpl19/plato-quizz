package com.app.plato_quizz.model;

public class GooglePojo {

    private String fullname;
    private String email;
    private String googleId;
    private String pictureUrl;
    private String locale;
    private Boolean emailVerified;

    public GooglePojo() {
    }

    public GooglePojo(String fullname, String email, String googleId, String pictureUrl, String locale, Boolean emailVerified) {
        this.fullname = fullname;
        this.email = email;
        this.googleId = googleId;
        this.pictureUrl = pictureUrl;
        this.locale = locale;
        this.emailVerified = emailVerified;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
