package com.choicemanager.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
    @Table(name= "goals")
    public class Goal {

        @Id
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

        }

    public String getCategory() {
        return category;
    }

    public void setCategory(String categoryGoal) {
        this.category = categoryGoal;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanationGoal) {
        this.explanation = explanationGoal;
    }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String goalName) {
            this.name = goalName;
        }
    }
