package com.choicemanager.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnswerWrapper {

    @ApiModelProperty(example = "[{\"question\": {\"id\": 4},\"user\": {\"id\": 1},\"value\": \"3\"}]")
    private List<Answer> answers;

    public AnswerWrapper() {
        answers = new ArrayList<>();
    }

}
