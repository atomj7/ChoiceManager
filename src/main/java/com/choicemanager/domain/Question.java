package com.choicemanager.domain;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "questions")
public class Question implements Serializable {

    @Getter
    @Id
    private Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Getter
    @NotNull
    @Column(name = "question")
    private String description;

    @Getter
    @NotNull
    @Column(name = "type")
    private String type;

}
