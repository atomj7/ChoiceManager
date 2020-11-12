package com.choicemanager.repository;

import com.choicemanager.domain.Goal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository  extends CrudRepository<Goal,Long> {
   public Goal findByName(String name);
}
