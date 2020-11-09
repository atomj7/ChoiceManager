package com.choicemanager.repository;

import com.choicemanager.domain.Answer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
    @Override
    Optional<Answer> findById(Long aLong);

    @Override
    Iterable<Answer> findAll();

    @Override
    <S extends Answer> S save(S entity);
}
