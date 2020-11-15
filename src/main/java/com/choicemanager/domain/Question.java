package com.choicemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Table(name = "questions")
public class Question implements Serializable {

    @Id
    private Long id;

    @JsonIgnore
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @NotNull
    @Column(name = "question")
    private String description;

    @NotNull
    @Column(name = "type")
    private String type;

}
