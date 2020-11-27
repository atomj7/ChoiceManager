package com.choicemanager.service;

import com.choicemanager.domain.User;
import com.choicemanager.repository.UserRepository;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository users;

    public AuthenticationService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails loadedUser;

        try {
            Optional <User> client = Optional.of(new User());
            if(users.findByUsername(username).isEmpty() && users.findByEmail(username).isPresent()) {
                client = users.findByEmail(username);
            }
            if(users.findByUsername(username).isPresent() && users.findByEmail(username).isEmpty()) {
                client = users.findByUsername(username);
            }
            loadedUser = new org.springframework.security.core.userdetails.User(
                    client.get().getUsername(), client.get().getPassword(),
                    client.get().getRoles());
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }
        return loadedUser;
    }

}