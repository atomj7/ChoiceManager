package com.choicemanager.service;

import com.choicemanager.domain.Goal;
import com.choicemanager.domain.Role;
import com.choicemanager.domain.User;
import com.choicemanager.repository.GoalRepository;
import com.choicemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;


@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private UserRepository userRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public boolean AddGoal(Goal goal, Optional<User> user)
    {
        if(goalRepository.findByName(goal.getName()) != null) {
           return false;
       }

        if(user.isPresent()) {
            goal.setUsers(Collections.singleton(user.get()));
            user.get().setGoals(Collections.singleton(goal));
        }
        else return false;
        goalRepository.save(goal);
        return true;

    }

    public boolean DeleteGoal(Goal goal)
    {
        goalRepository.delete(goal);
        return true;
    }

    public Iterable<Goal> GetGoals() {
        return goalRepository.findAll();
    }

}
