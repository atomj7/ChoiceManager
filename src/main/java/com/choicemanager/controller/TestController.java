package com.choicemanager.controller;

import com.choicemanager.domain.*;
import com.choicemanager.security.CurrentUser;
import com.choicemanager.service.TestService;
import com.choicemanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class TestController {

    private final UserService userService;
    private final TestService testService;

    public TestController(UserService userService, TestService testService) {
        this.userService = userService;
        this.testService = testService;
    }

    @Operation(summary = "Get Category or list of Categories with Questions")
    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTest(@CurrentUser UserPrincipal userPrincipal,
                                     @Parameter(description = "ID of a specific category if required") Long pageNumber) {
        CustomErrorResponse errors = new CustomErrorResponse();
        CategoryWrapper categories = testService.getCategories(errors, pageNumber);

        if (errors.getError() == null || errors.getError().isEmpty()) {
            return ResponseEntity.ok(categories);
        }
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Put answers to database")
    @PostMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveAnswers(@CurrentUser UserPrincipal userPrincipal,
                                         @RequestBody @Parameter(name = "answerList",
                                                 description = "Answer list wrapped in \"answers\" object")
                                                 AnswerWrapper answerWrapper) {
        User user = userService.getCurrentUser(userPrincipal);
        Long userId = user.getId();

        CustomErrorResponse errors = new CustomErrorResponse();
        ArrayList<Answer> recordedAnswerList = new ArrayList<>();

        testService.saveAnswers(errors, userId, answerWrapper, recordedAnswerList);

        if (errors.getError() == null || errors.getError().isEmpty()) {
            return ResponseEntity.ok(recordedAnswerList.stream().map(Answer::getId));
        }
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

}
