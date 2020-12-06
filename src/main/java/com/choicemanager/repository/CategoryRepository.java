package com.choicemanager.repository;

import com.choicemanager.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Iterable<Category> findAll();

    long count();
}