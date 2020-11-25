package com.choicemanager.repository;

import com.choicemanager.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByActivationCode(String code);
    @Override
    Optional<User> findById(Long aLong);
}
