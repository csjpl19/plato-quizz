package com.app.plato_quizz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.plato_quizz.model.StatutPQ;
import com.app.plato_quizz.service.UserAccountService;

@Controller
public class AuthController {

    private final UserAccountService userAccountService;

    public AuthController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/register")
    public String register(
        @RequestParam String fullname,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam(required = false) String googleId,
        @RequestParam(required = false) String statut
    ) {
        try {
            userAccountService.registerManual(fullname, email, password, googleId, parseStatut(statut));
            return "redirect:/login?registered";
        } catch (IllegalArgumentException exception) {
            return "redirect:/register?error";
        }
    }

    private StatutPQ parseStatut(String rawStatut) {
        if (rawStatut == null || rawStatut.isBlank()) {
            return StatutPQ.OFFLINE;
        }
        try {
            return StatutPQ.valueOf(rawStatut.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            return StatutPQ.OFFLINE;
        }
    }
}
