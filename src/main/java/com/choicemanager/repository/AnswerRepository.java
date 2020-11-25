package com.choicemanager.repository;

import com.choicemanager.domain.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Optional;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
    @Override
    Optional<Answer> findById(Long aLong);

    @Override
    Iterable<Answer> findAll();

    ArrayList<Answer> findAllByUserId(Long id);

    @Override
    <S extends Answer> S save(S entity);
}
