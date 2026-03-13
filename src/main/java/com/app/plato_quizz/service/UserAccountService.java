package com.app.plato_quizz.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.plato_quizz.model.StatutPQ;
import com.app.plato_quizz.model.UserPQ;
import com.app.plato_quizz.repository.ClientRepository;

@Service
public class UserAccountService {

    private static final String PROVIDER_GOOGLE = "GOOGLE";
    private static final String PROVIDER_MANUAL = "MANUAL";

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserPQ registerManual(String fullname, String email, String password, String googleId, StatutPQ statut) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email obligatoire");
        }
        if (fullname == null || fullname.isBlank()) {
            throw new IllegalArgumentException("Nom complet obligatoire");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Mot de passe obligatoire");
        }
        if (clientRepository.countByEmail(email) > 0) {
            throw new IllegalArgumentException("Email deja utilise");
        }

        UserPQ user = new UserPQ();
        user.setFullname(fullname.trim());
        user.setEmail(email.trim());
        user.setPassword(passwordEncoder.encode(password));
        user.setPictureUrl(null);
        user.setAuthProvider(PROVIDER_MANUAL);
        user.setStatut(statut != null ? statut : StatutPQ.OFFLINE);

        return clientRepository.save(user);
    }

    @Transactional
    public UserPQ registerOrUpdateGoogleUser(OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();

        String email = sanitize(readString(attributes, "email"));
        String fullname = sanitize(readString(attributes, "name"));
        String googleId = sanitize(readString(attributes, "sub"));
        String pictureUrl = sanitize(readString(attributes, "picture"));

        if (email == null) {
            throw new IllegalStateException("Le compte Google ne renvoie pas d'email");
        }

        Optional<UserPQ> byGoogleId = googleId == null
            ? Optional.empty()
            : clientRepository.findByGoogleId(googleId);

        UserPQ user = byGoogleId.orElseGet(() -> clientRepository.findByEmail(email).orElseGet(UserPQ::new));

        user.setEmail(email);
        user.setFullname(fullname != null ? fullname : email);
        user.setGoogleId(googleId);
        user.setPictureUrl(pictureUrl);
        user.setAuthProvider(PROVIDER_GOOGLE);
        user.setStatut(StatutPQ.ONLINE);

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        }

        return clientRepository.save(user);
    }

    @Transactional
    public void markOnlineByEmail(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        clientRepository.findByEmail(email).ifPresent(user -> {
            user.setStatut(StatutPQ.ONLINE);
            clientRepository.save(user);
        });
    }

    @Transactional
    public void markOfflineByEmail(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        clientRepository.findByEmail(email).ifPresent(user -> {
            user.setStatut(StatutPQ.OFFLINE);
            clientRepository.save(user);
        });
    }

    @Transactional(readOnly = true)
    public Optional<UserPQ> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return clientRepository.findByEmail(email);
    }

    public String resolveEmail(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User oauth2User) {
            return sanitize(readString(oauth2User.getAttributes(), "email"));
        }
        if (principal instanceof UserDetails userDetails) {
            return sanitize(userDetails.getUsername());
        }
        return sanitize(authentication.getName());
    }

    private String readString(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        return value == null ? null : String.valueOf(value);
    }

    private String sanitize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
