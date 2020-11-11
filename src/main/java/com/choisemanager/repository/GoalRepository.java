package com.choisemanager.repository;

import com.choisemanager.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository  extends CrudRepository<Goal,Long> {
   public Goal findByName(String name);
}
