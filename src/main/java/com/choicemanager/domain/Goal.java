package com.choicemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name= "goals")
public class Goal {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String explanation;

    @Column(name="isDone")
    @NotNull
    private boolean isDone;

    @JsonIgnore
    @ManyToMany(mappedBy = "goals")
    private Set<User> users;

    @EqualsAndHashCode.Exclude
    @OneToMany( mappedBy="goals", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks;

    public Goal () {}

    public Goal(long id, String Name, String explanation){
        this.id = id;
        this.name = Name;
        this. explanation = explanation;
    }

    public Goal(Goal goal){
        this.id = goal.getId();
        this.name = goal.getName();
        this.explanation = goal.getExplanation();
        this.isDone = goal.isDone();
        this.users = getUsers();
        this.tasks = goal.getTasks();

    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
