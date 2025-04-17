package com.nevrcode.expense_tracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nevrcode.expense_tracker.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>{
    Optional<User> findFirstByToken(String token);
}
