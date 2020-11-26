package com.choicemanager.service;

import com.choicemanager.domain.User;
import com.choicemanager.domain.UserPrincipal;
import com.choicemanager.repository.UserRepository;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        if (userRepository.findByUsername(usernameOrEmail).isPresent()) {

            return UserPrincipal.create(userRepository.findByUsername(usernameOrEmail).get());
        } else if (userRepository.findByEmail(usernameOrEmail).isPresent()) {

            return UserPrincipal.create(userRepository.findByEmail(usernameOrEmail).get());
        } else {

            throw new UsernameNotFoundException("User not found");
        }
    }

}