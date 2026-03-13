package com.app.plato_quizz.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.plato_quizz.model.UserPQ;
import com.app.plato_quizz.repository.ClientRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    public CustomUserDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPQ user = clientRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Aucun compte trouve pour cet email."));

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new UsernameNotFoundException("Ce compte utilise uniquement la connexion Google.");
        }

        return User.withUsername(user.getEmail())
            .password(user.getPassword())
            .roles("USER")
            .build();
    }
}
