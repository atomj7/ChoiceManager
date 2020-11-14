package com.choicemanager.service;

import com.choicemanager.domain.User;
import com.choicemanager.repository.UserRepository;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
            User client = new User();
            if(users.findByLogin(username)== null && users.findByEmail(username) != null) {
                client = users.findByEmail(username);
            }
            if(users.findByLogin(username) != null && users.findByEmail(username) == null) {
                client = users.findByLogin(username);
            }
            loadedUser = new org.springframework.security.core.userdetails.User(
                    client.getUsername(), client.getPassword(),
                    client.getRoles());
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }
        return loadedUser;
    }

}