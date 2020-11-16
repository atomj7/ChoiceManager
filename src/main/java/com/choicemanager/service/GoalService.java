package com.choicemanager.service;

import com.choicemanager.domain.Goal;
import com.choicemanager.domain.Role;
import com.choicemanager.domain.User;
import com.choicemanager.repository.GoalRepository;
import com.choicemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


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
            for (Goal newGoal : Collections.singleton(goal))
            {
                user.get().getGoals().add(newGoal);
            }

            goal.setUsers(goal.getUsers());
            user.get().setGoals(user.get().getGoals());
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

    public ArrayList<Goal> GetGoals(Optional<User> userOptional) {
        User user = new User();
        user = userOptional.get();
        Set<Goal> goals = user.getGoals();
        ArrayList<Goal> goalList = new ArrayList<Goal>();
        for(Goal goal1 : goals){
            goal1.setUsers(null);
            goalList.add(goal1);
        }

        return goalList;
    }

}
