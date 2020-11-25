package com.choicemanager.repository;

import com.choicemanager.domain.Goal;
import io.micrometer.core.lang.NonNullApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface GoalRepository  extends CrudRepository<Goal,Long> {
    Goal findByName(String name);

    @Override
    Optional<Goal> findById (Long Id);
}
