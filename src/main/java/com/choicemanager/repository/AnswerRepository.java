package com.choicemanager.repository;

import com.choicemanager.domain.Answer;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
    @Override
    Optional<Answer> findById(Long aLong);

    @Override
    Iterable<Answer> findAll();

    ArrayList<Answer> findALlByQuestionId(Long id);

    ArrayList<Answer> findAllByUserId(String id);

    @Override
    <S extends Answer> S save(S entity);
}
