package com.choicemanager.service;

import com.choicemanager.domain.Goal;
import com.choicemanager.domain.GoalWrapper;
import com.choicemanager.domain.Task;
import com.choicemanager.domain.User;
import com.choicemanager.repository.GoalRepository;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class GoalService {

    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public boolean AddGoal(Goal goal, User user) {
        if(goalRepository.findByName(goal.getName()) != null) {
           return false;
       }
            for (Goal newGoal : Collections.singleton(goal))
            {
                user.getGoals().add(newGoal);
            }
            goal.setUsers(goal.getUsers());

           for (Task newTask : goal.getTasks())
            {
               newTask.setGoals(goal);
               goal.getTasks().add(newTask);
            }
            goal.setTasks(goal.getTasks());

        goalRepository.save(goal);
        return true;
    }

    public boolean EditGoal(Goal goal){
        for (Task newTask : goal.getTasks())
        {
            newTask.setGoals(goal);
            goal.getTasks().add(newTask);
        }
        goalRepository.save(goal);

        return true;
    }

    public boolean DeleteGoal(Long id) {
        goalRepository.deleteById(id);

        return true;
    }

    public GoalWrapper GetGoals(User userOptional) {
        Set<Goal> goals =  userOptional.getGoals();
        ArrayList<Goal> goalArrayList = new ArrayList<>(goals);
        GoalWrapper goalWrapper = new GoalWrapper();
        goalWrapper.setGoals(goalArrayList);
            return goalWrapper;
    }

}
