package com.choicemanager.service;

import com.choicemanager.domain.Goal;
import com.choicemanager.domain.GoalWrapper;
import com.choicemanager.domain.Task;
import com.choicemanager.domain.User;
import com.choicemanager.repository.GoalRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class GoalService {

    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public void AddGoal(Goal goal, User user) {

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

        goal.setProgress(0);
        goalRepository.save(goal);
    }

    public boolean EditGoal(Goal goal){
        double value =0;
        for (Task newTask : goal.getTasks())
        {
            if(newTask.isDone())
            {
                value++;
            }
            newTask.setGoals(goal);
            goal.getTasks().add(newTask);
        }
        if(goal.isDone())
        {
            goal.setProgress(100);
            for (Task newTask : goal.getTasks())
            {
                newTask.setDone(true);
            }

        }
        else if(value == 0)
        {
            goal.setProgress(0);
        }
        else
        goal.setProgress(value/(goal.getTasks().size()+1)*100);
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
        goalArrayList.sort(Comparator.comparing(Goal::getId));
        GoalWrapper goalWrapper = new GoalWrapper();
        goalWrapper.setGoals(goalArrayList);
            return goalWrapper;
    }


}
