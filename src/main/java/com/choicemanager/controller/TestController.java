package com.choicemanager.controller;

import com.choicemanager.domain.Answer;
import com.choicemanager.domain.AnswerWrapper;
import com.choicemanager.domain.CategoryWrapper;
import com.choicemanager.domain.CustomErrorResponse;
import com.choicemanager.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Operation(summary = "Get Category or list of Categories with Questions")
    @GetMapping("/test")
    public ResponseEntity<?> getTest(@Parameter(description = "ID of a specific category if required") Long id) {
        CustomErrorResponse errors = new CustomErrorResponse();
        CategoryWrapper categories = testService.getCategories(errors, id);

        if (errors.getError() == null || errors.getError().isEmpty()) {
            return ResponseEntity.ok(categories);
        }
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Put answers to database")
    @PostMapping("/test")
    public ResponseEntity<?> saveAnswers(@RequestBody
                                         @Parameter(name = "answerList",
                                                 description = "Answer list wrapped in \"answers\" object")
                                                 AnswerWrapper answerWrapper) {
        CustomErrorResponse errors = new CustomErrorResponse();
        ArrayList<Answer> createdAnswerList = new ArrayList<>();

        testService.saveAnswers(errors, answerWrapper, createdAnswerList);

        if (errors.getError() == null || errors.getError().isEmpty()) {
            return ResponseEntity.ok(createdAnswerList.stream().map(Answer::getId));
        }
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

}
