package com.choicemanager.domain;

import com.sun.istack.NotNull;
import lombok.Data;

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

    @Column(name="name")
    @NotNull
    private String name;

    @Column(name="category")
    @NotNull
    private String category;

    @Column(name="explanation")
    @NotNull
    private String explanation;

    @ManyToMany(mappedBy = "goals")
    private Set<User> users;



    public Goal () {}

    public Goal(long id, String Name, String category, String explanation){
        this.id = id;
        this.name = Name;
        this.category = category;
        this. explanation = explanation;
    }

    public Goal(Goal goal){
        this.id = getId();
        this.name = getName();
        this.category = getCategory();
        this.explanation = getExplanation();
        this.users = getUsers();

    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
