package com.app.plato_quizz.controller;

import com.app.plato_quizz.model.Defi;
import com.app.plato_quizz.model.GooglePojo;
import com.app.plato_quizz.model.UserPQ;
import com.app.plato_quizz.model.UserStatistic;
import com.app.plato_quizz.service.DefiService;
import com.app.plato_quizz.service.UserAccountService;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DefiController {

    private final UserAccountService userAccountService;
    private final DefiService defiService;

    public DefiController(UserAccountService userAccountService, DefiService defiService) {
        this.userAccountService = userAccountService;
        this.defiService = defiService;
    }

    @GetMapping("/defis")
    public String listDefis(Authentication authentication, Model model) {
        UserPQ connectedUser = resolveConnectedUser(authentication);
        if (connectedUser == null) {
            return "redirect:/login";
        }

        enrichUserContext(authentication, connectedUser, model);

        model.addAttribute("defis", defiService.listAvailableDefisForUser(connectedUser));

        Optional<UserStatistic> statisticOptional = defiService.findStatistic(connectedUser);
        model.addAttribute("totalDefiPoints", statisticOptional.map(UserStatistic::getTotalDefiPoints).orElse(0));
        model.addAttribute("totalDefisCompleted", statisticOptional.map(UserStatistic::getTotalDefisCompleted).orElse(0));
        model.addAttribute("totalDefiQuestions", statisticOptional.map(UserStatistic::getTotalDefiQuestions).orElse(0));
        model.addAttribute("totalDefiCorrectAnswers", statisticOptional.map(UserStatistic::getTotalDefiCorrectAnswers).orElse(0));

        return "defis";
    }

    @GetMapping("/defis/{defiId}")
    public String playDefi(@PathVariable Long defiId, Authentication authentication, Model model) {
        UserPQ connectedUser = resolveConnectedUser(authentication);
        if (connectedUser == null) {
            return "redirect:/login";
        }

        Optional<Defi> defiOptional = defiService.findPlayableDefi(defiId);
        if (defiOptional.isEmpty()) {
            return "redirect:/defis";
        }

        Defi defi = defiOptional.get();
        enrichUserContext(authentication, connectedUser, model);
        model.addAttribute("defi", defi);
        model.addAttribute("existingParticipation", defiService.findParticipation(connectedUser, defi).orElse(null));

        return "defi-play";
    }

    @PostMapping("/defis/{defiId}/submit")
    public String submitDefi(
        @PathVariable Long defiId,
        @RequestParam Map<String, String> answers,
        Authentication authentication,
        RedirectAttributes redirectAttributes
    ) {
        UserPQ connectedUser = resolveConnectedUser(authentication);
        if (connectedUser == null) {
            return "redirect:/login";
        }

        try {
            DefiService.DefiSubmissionResult result = defiService.submitDefiAnswers(defiId, connectedUser, answers);
            redirectAttributes.addFlashAttribute("submissionResult", result);
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("defiError", exception.getMessage());
            return "redirect:/defis";
        }

        return "redirect:/defis/" + defiId;
    }

    private UserPQ resolveConnectedUser(Authentication authentication) {
        String email = userAccountService.resolveEmail(authentication);
        return userAccountService.findByEmail(email).orElse(null);
    }

    private void enrichUserContext(Authentication authentication, UserPQ connectedUser, Model model) {
        String displayName = connectedUser.getFullname();
        if (displayName == null || displayName.isBlank()) {
            displayName = connectedUser.getEmail();
        }
        if (displayName == null || displayName.isBlank()) {
            displayName = "Utilisateur";
        }

        boolean isGoogleLogin = "GOOGLE".equalsIgnoreCase(connectedUser.getAuthProvider());

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

        model.addAttribute("displayName", displayName);
        model.addAttribute("pictureUrl", connectedUser.getPictureUrl());
        model.addAttribute("isGoogleLogin", isGoogleLogin);
        model.addAttribute(
            "connectionTypeMessage",
            isGoogleLogin ? "Connexion avec Google" : "Connexion manuelle"
        );
        model.addAttribute("googleProfile", googleProfile);
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
