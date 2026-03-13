package com.app.plato_quizz.config;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

import com.app.plato_quizz.service.UserAccountService;

@Component
public class SessionDestroyedListener implements ApplicationListener<SessionDestroyedEvent> {

    private final UserAccountService userAccountService;

    public SessionDestroyedListener(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        for (SecurityContext securityContext : event.getSecurityContexts()) {
            Authentication authentication = securityContext.getAuthentication();
            String email = userAccountService.resolveEmail(authentication);
            userAccountService.markOfflineByEmail(email);
        }
    }
}
