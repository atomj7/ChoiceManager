package com.choicemanager.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "answers")
public class Answer implements Serializable {

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    Question question;

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Getter
    @Column(name = "value")
    @NotNull
    private String value;

    @Setter
    @Getter
    @NotNull
    @Column(name = "user_id")
    private Long userId;

}
