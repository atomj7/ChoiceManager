package com.choicemanager.repository;

import com.choicemanager.domain.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository  extends CrudRepository<Task,Long> {

}
