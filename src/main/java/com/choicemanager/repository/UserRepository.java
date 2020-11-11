package com.choicemanager.repository;

import com.choicemanager.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);
    User findByGoogleAuthId(String googleAuthId);
    User findByEmail(String email);
    User findByActivationCode(String code);

    @Override
    Optional<User> findById(Long aLong);
}
