package com.choicemanager.repository;

import com.choicemanager.domain.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    Optional<Question> findById(Long id);
}
