package com.choicemanager.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnswerWrapper {

    private List<Answer> answers;

    public AnswerWrapper() {
        answers = new ArrayList<>();
    }

}
