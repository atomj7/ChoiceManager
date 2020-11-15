package com.choicemanager.controller;

import com.choicemanager.domain.*;
import com.choicemanager.repository.AnswerRepository;
import com.choicemanager.repository.CategoryRepository;
import com.choicemanager.repository.QuestionRepository;
import com.choicemanager.repository.UserRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TestController {

    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public TestController(AnswerRepository answerRepository, CategoryRepository categoryRepository,
                          QuestionRepository questionRepository, UserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "Get Category or list of Categories with Questions", response = CategoryWrapper.class)
    @ApiImplicitParams({@ApiImplicitParam(
            name = "id",
            value = "ID of a specific category if required",
            paramType = "query",
            dataType = "Long"
    )})
    @GetMapping("/test")
    public ResponseEntity<?> getTest(@RequestParam(required = false) Long id) {
        CategoryWrapper categories = new CategoryWrapper();
        CustomErrorResponse errors = new CustomErrorResponse();

        if (id == null) {
            ArrayList<Category> categoryList = (ArrayList<Category>) categoryRepository.findAll();
            if (categoryList.isEmpty()) {
                errors.setTimestamp(LocalDateTime.now());
                errors.setError("No categories were found");
                errors.setStatus(HttpStatus.NOT_FOUND.value());
            } else {
                categories.setCategories(categoryList);
            }
        } else {
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isPresent()) {
                categories.setCategories(new ArrayList<>(List.of(optionalCategory.get())));
            } else {
                errors.setTimestamp(LocalDateTime.now());
                errors.setError("Category id not found : " + id);
                errors.setStatus(HttpStatus.NOT_FOUND.value());
            }
        }

        if (errors.getError() == null || errors.getError().isEmpty()) {
            return ResponseEntity.ok(categories);
        }
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Put answers to database", response = AnswerWrapper.class)
    @PostMapping("/test")
    public ResponseEntity<?> save(@RequestBody
                                  @Parameter(
                                          name = "answerList",
                                          description = "Answer list wrapped in \"answers\" object")
                                          Iterable<Answer> answerList) {
        ArrayList<Answer> createdAnswerList = new ArrayList<>();
        for (Answer answer : answerList) {
            Long userId = answer.getUser().getId();
            Long questionId = answer.getQuestion().getId();
            String value = answer.getValue();
            Optional<Question> questionOptional = questionRepository.findById(questionId);

            if (questionOptional.isEmpty()) {
                return new ResponseEntity<>(new CustomErrorResponse(
                        "Incorrect or non-existent question id : " + questionId,
                        HttpStatus.NOT_FOUND.value()
                ), HttpStatus.NOT_FOUND);
            }

            Question question = questionOptional.get();
            if (question.getType().equals("scale")) {
                if (value.matches("-?\\d+(\\.\\d+)?")) {
                    double doubleValue = Double.parseDouble(value);
                    if (Double.parseDouble(value) < 0) {
                        return new ResponseEntity<>(new CustomErrorResponse(
                                "The value must be positive",
                                HttpStatus.BAD_REQUEST.value()
                        ), HttpStatus.BAD_REQUEST);
                    }
                    answer.setValue(Double.toString(doubleValue));//"1" to "1.0"
                } else {
                    return new ResponseEntity<>(new CustomErrorResponse(
                            "Value type does not match the question type. Value must be of type Double",
                            HttpStatus.BAD_REQUEST.value()
                    ), HttpStatus.BAD_REQUEST);
                }
            }

            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(new CustomErrorResponse(
                        "Incorrect or non-existent user id : " + userId,
                        HttpStatus.NOT_FOUND.value()
                ), HttpStatus.NOT_FOUND);
            }

            User user = userOptional.get();
            answer.setQuestion(question);
            answer.setUser(user);
            Answer createdAnswer = answerRepository.save(answer);
            if (createdAnswer == null) {
                return ResponseEntity.notFound().build();
            } else {
                createdAnswerList.add(createdAnswer);
            }
        }
        return ResponseEntity.ok(createdAnswerList.stream().map(Answer::getId));
    }

}
