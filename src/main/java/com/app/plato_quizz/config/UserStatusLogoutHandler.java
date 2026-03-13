package com.app.plato_quizz.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.app.plato_quizz.service.UserAccountService;

@Component
public class UserStatusLogoutHandler implements LogoutHandler {

    private final UserAccountService userAccountService;

    public UserStatusLogoutHandler(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String email = userAccountService.resolveEmail(authentication);
        userAccountService.markOfflineByEmail(email);
    }
}
