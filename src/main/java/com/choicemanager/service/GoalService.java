package com.choicemanager.service;

import com.choicemanager.domain.*;
import com.choicemanager.repository.GoalRepository;
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

    public GoalDto EditGoal(Goal goal){
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

        GoalDto goalDto = new GoalDto();
        goalDto.setProgress(goal.getProgress());
        return goalDto;
    }

    public boolean DeleteGoal(Long id) {
        goalRepository.deleteById(id);
        return true;
    }

    public GoalWrapper GetGoals(User userOptional) {
        Set<Goal> goals =  userOptional.getGoals();
        ArrayList<Goal> newGoalArrayList = new ArrayList<>();
        for (Goal newGoal : goals)
        {
            ArrayList<Task> taskArrayList = new ArrayList<>(newGoal.getTasks());
            taskArrayList.sort(Comparator.comparing(Task::getId));
            Set<Task> taskSet = new LinkedHashSet<>(taskArrayList);
            newGoal.setTasks(taskSet);
            newGoalArrayList.add(newGoal);
        }
        newGoalArrayList.sort(Comparator.comparing(Goal::getId));
        GoalWrapper goalWrapper = new GoalWrapper();
        goalWrapper.setGoals(newGoalArrayList);
            return goalWrapper;
    }


}
