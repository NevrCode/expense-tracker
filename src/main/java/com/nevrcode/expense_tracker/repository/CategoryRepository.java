package com.nevrcode.expense_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nevrcode.expense_tracker.entity.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    
}
