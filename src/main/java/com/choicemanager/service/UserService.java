package com.choicemanager.service;

import com.choicemanager.domain.Role;
import com.choicemanager.domain.User;
import com.choicemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("UserService")
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean addUser(User userData) {
        if (checkIfUserExistByEmail(userData.getEmail())) {
            return false;
        } else if (checkIfUserExistByLogin(userData.getLogin())) {
            return false;
        }
        User newUser = new User(userData);
        newUser.setActive(true);
        newUser.setRole(Collections.singleton(Role.USER));
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        return true;
    }

    public boolean checkIfUserExistByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean checkIfUserExistByLogin(String login) {
        return userRepository.findByLogin(login) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }
}
