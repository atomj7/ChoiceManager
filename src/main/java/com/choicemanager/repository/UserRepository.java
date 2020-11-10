package com.choicemanager.repository;

import com.choicemanager.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    User findByLogin(String login);

    User findByEmail(String login);
}
