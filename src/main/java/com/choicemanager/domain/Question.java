package com.choicemanager.domain;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Getter
    @Column(name = "question")
    @NotNull
    private String description;

    @Getter
    @Column(name = "type")
    @NotNull
    private String type;

}
