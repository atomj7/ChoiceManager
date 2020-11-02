package com.choicemanager.repository;

import com.choicemanager.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    public User findByLogin(String login);
    public User findByEmail(String login);
}
