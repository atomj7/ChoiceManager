package com.choicemanager.domain;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Getter
    @Id
    private Long id;

    @Getter
    @Column(name = "category")
    @NotNull
    private String name;

    @Getter
    @OneToMany(mappedBy = "category")
    private List<Question> questions;

}
