package com.app.plato_quizz.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.app.plato_quizz.service.UserAccountService;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserAccountService userAccountService;

    public LoginSuccessHandler(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oauth2User) {
            userAccountService.registerOrUpdateGoogleUser(oauth2User);
        } else {
            userAccountService.markOnlineByEmail(authentication.getName());
        }

        response.sendRedirect("/accueil");
    }
}
