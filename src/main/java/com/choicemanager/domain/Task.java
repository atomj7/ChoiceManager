package com.choicemanager.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "tasks")
public class Task {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="name")
    @NotNull
    private String name;

    @Column(name="isDone")
    @NotNull
    private boolean isDone;

    @ManyToMany(mappedBy = "tasks",cascade = CascadeType.ALL)
    private Set<Goal> goals;

    public Task() {}

    public Task(String name, boolean isDone){
        this.name = name;
        this.isDone = isDone;
    }

    public Task(Task task){
        this.id = task.getId();
        this.name = task.getName();
        this.isDone = task.isDone();
        this.goals= task.getGoals();

    }

}
