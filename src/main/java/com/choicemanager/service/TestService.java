package com.choicemanager.service;

import com.choicemanager.domain.*;
import com.choicemanager.repository.AnswerRepository;
import com.choicemanager.repository.CategoryRepository;
import com.choicemanager.repository.QuestionRepository;
import com.choicemanager.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("TestService")
public class TestService {

    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public TestService(CategoryRepository categoryRepository,
                       QuestionRepository questionRepository,
                       UserRepository userRepository,
                       AnswerRepository answerRepository) {
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }

    public CategoryWrapper getCategories(CustomErrorResponse errors, Long id) {
        CategoryWrapper categories = new CategoryWrapper();

        if (id == null) {
            ArrayList<Category> categoryList = (ArrayList<Category>) categoryRepository.findAll();
            if (categoryList.isEmpty()) {
                errors.setError("No categories were found");
                errors.setStatus(HttpStatus.NOT_FOUND.value());
            } else {
                categories.setCategories(categoryList);
            }
        } else {
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isPresent()) {
                categories.setCategories(new ArrayList<>(List.of(optionalCategory.get())));
                categories.setCategoryNumber(categoryRepository.count());
            } else {
                errors.setError("Category id not found : " + id);
                errors.setStatus(HttpStatus.NOT_FOUND.value());
            }
        }

        return categories;
    }

    public void saveAnswers(CustomErrorResponse errors, Long userId, AnswerWrapper answerWrapper, ArrayList<Answer> recordedAnswerList) {
        ArrayList<Answer> tempAnswerList = new ArrayList<>();

        for (Answer answer : answerWrapper.getAnswers()) {
            Long questionId = answer.getQuestion().getId();
            String value = answer.getValue();
            Optional<Question> questionOptional = questionRepository.findById(questionId);

            if (questionOptional.isEmpty()) {
                errors.setError("Incorrect or non-existent question id : " + questionId);
                errors.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }

            Question question = questionOptional.get();
            if (question.getType().equals("scale")) {
                if (value.matches("-?\\d+(\\.\\d+)?")) {
                    double doubleValue = Double.parseDouble(value);
                    if (Double.parseDouble(value) < 0) {
                        errors.setError("The value must be positive");
                        errors.setStatus(HttpStatus.BAD_REQUEST.value());
                        return;
                    }
                    answer.setValue(Double.toString(doubleValue));//"1" to "1.0"
                } else {
                    errors.setError("Value type does not match the question type. Value must be of type Double");
                    errors.setStatus(HttpStatus.BAD_REQUEST.value());
                    return;
                }
            }

            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                errors.setError("Incorrect or non-existent user id : " + userId);
                errors.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }

            User user = userOptional.get();
            answer.setQuestion(question);
            answer.setUser(user);
            tempAnswerList.add(answer);
        }

        List<Answer> tempRecordedList =
                StreamSupport.stream(answerRepository.saveAll(tempAnswerList).spliterator(), false)
                        .collect(Collectors.toList());
        if (tempRecordedList.isEmpty()) {
            errors.setError("No answers were saved");
            errors.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            for (Answer tempAnswer : tempRecordedList) {
                if (tempAnswer.getUser().isTested()) {
                    break;
                }
                if (tempAnswer.getQuestion().getCategory().getId() == categoryRepository.count()) {
                    User user = userRepository.findById(userId).get();
                    user.setTested(true);
                    userRepository.save(user);
                }
            }
            recordedAnswerList.addAll(tempRecordedList);
        }
    }

}
