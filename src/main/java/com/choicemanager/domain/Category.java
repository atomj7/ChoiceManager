package com.choicemanager.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    private Long id;

    @Column(name = "category")
    @NotNull
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Question> questions;

}
