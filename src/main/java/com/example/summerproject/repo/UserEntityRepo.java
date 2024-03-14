package com.example.summerproject.repo;

import com.example.summerproject.entity.UserEntity;
import com.example.summerproject.generic.GenericSoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepo extends GenericSoftDeleteRepository<UserEntity,Long> {
    Optional<UserEntity> findByUsername(String username);
}

