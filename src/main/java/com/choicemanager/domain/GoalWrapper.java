package com.choicemanager.domain;


import lombok.Data;

import java.util.ArrayList;

@Data
public class GoalWrapper {
    private ArrayList<Goal> goals;

    public GoalWrapper(){ goals = new ArrayList<>();}
}
