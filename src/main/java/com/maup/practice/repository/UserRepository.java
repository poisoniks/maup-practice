package com.maup.practice.repository;

import com.maup.practice.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);
    boolean existsByEmail(String email);
}
