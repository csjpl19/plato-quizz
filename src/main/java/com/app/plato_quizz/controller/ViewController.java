package com.app.plato_quizz.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.app.plato_quizz.model.GooglePojo;
import com.app.plato_quizz.model.UserPQ;
import com.app.plato_quizz.service.UserAccountService;

@Controller
public class ViewController {

    private final UserAccountService userAccountService;

    public ViewController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/quick-quiz")
    public String quickquizz(){
        return "quick-quiz";
    }

    @GetMapping("/accueil")
    public String accueil(Authentication authentication, Model model){
        String email = userAccountService.resolveEmail(authentication);
        UserPQ connectedUser = userAccountService.findByEmail(email).orElse(null);

        String displayName = connectedUser != null && connectedUser.getFullname() != null
            ? connectedUser.getFullname()
            : email;
        String pictureUrl = connectedUser != null ? connectedUser.getPictureUrl() : null;
        boolean isGoogleLogin = connectedUser != null && "GOOGLE".equalsIgnoreCase(connectedUser.getAuthProvider());

        GooglePojo googleProfile = null;
        if (isGoogleLogin && authentication != null && authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            Map<String, Object> attributes = oauth2User.getAttributes();
            googleProfile = new GooglePojo();
            googleProfile.setFullname(connectedUser.getFullname());
            googleProfile.setEmail(connectedUser.getEmail());
            googleProfile.setGoogleId(connectedUser.getGoogleId());
            googleProfile.setPictureUrl(connectedUser.getPictureUrl());
            googleProfile.setLocale(readString(attributes, "locale"));
            googleProfile.setEmailVerified(readBoolean(attributes, "email_verified"));
        }

        if (displayName == null || displayName.isBlank()) {
            displayName = "Utilisateur";
        }

        model.addAttribute("displayName", displayName);
        model.addAttribute("pictureUrl", pictureUrl);
        model.addAttribute("isGoogleLogin", isGoogleLogin);
        model.addAttribute(
            "connectionTypeMessage",
            isGoogleLogin ? "Connexion avec Google" : "Connexion manuelle"
        );
        model.addAttribute("googleProfile", googleProfile);

        return "accueil";
    }

    private String readString(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        return value == null ? null : String.valueOf(value);
    }

    private Boolean readBoolean(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value == null) {
            return null;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }
}
