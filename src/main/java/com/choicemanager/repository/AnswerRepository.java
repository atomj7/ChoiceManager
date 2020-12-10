package com.choicemanager.repository;

import com.choicemanager.domain.Answer;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
    Optional<Answer> findById(Long aLong);

    Iterable<Answer> findAll();

    ArrayList<Answer> findAllByUserId(Long id);

    <S extends Answer> S save(S entity);

    <S extends Answer> Iterable<S> saveAll(Iterable<S> entities);

    Iterable<Answer> findAllById(Iterable<Long> longs);
}