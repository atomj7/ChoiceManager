package com.choicemanager.service;

import com.choicemanager.domain.Goal;
import com.choicemanager.domain.Task;
import com.choicemanager.domain.User;
import com.choicemanager.repository.GoalRepository;
import com.choicemanager.repository.TaskRepository;
import com.choicemanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GoalService {

    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @Transactional
    public boolean AddGoal(Goal goal, Optional<User> user) {
        if(goalRepository.findById(goal.getId()).isPresent()) {
           return false;
       }
        if(user.isPresent()) {
            for (Goal newGoal : Collections.singleton(goal))
            {
                user.get().getGoals().add(newGoal);
            }
            goal.setUsers(goal.getUsers());

           for (Task newTask : goal.getTasks())
            {
                goal.getTasks().add(newTask);
            }
            goal.setTasks(goal.getTasks());

        }
        else return false;
        goalRepository.save(goal);
        return true;
    }

    public boolean EditGoal(Goal goal){

        goalRepository.save(goal);

        return true;
    }

    public boolean DeleteGoal(Goal goal) {
        goalRepository.delete(goal);
        return true;
    }

    @Transactional
    public ArrayList<Goal> GetGoals(Optional<User> userOptional) {
            User user = userOptional.get();
            Set<Goal> goals = user.getGoals();
            return new ArrayList<Goal>(goals);
    }

}
