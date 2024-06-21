package com.vinicius.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vinicius.todo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
