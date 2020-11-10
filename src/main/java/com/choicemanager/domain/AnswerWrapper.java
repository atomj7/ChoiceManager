package com.choicemanager.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class AnswerWrapper {
    @Getter
    @Setter
    private List<Answer> answers;

    public AnswerWrapper() {
        answers = new ArrayList<>();
    }
}
