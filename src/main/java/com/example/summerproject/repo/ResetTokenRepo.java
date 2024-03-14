package com.example.summerproject.repo;

import com.example.summerproject.entity.ResetTokenEntity;
import com.example.summerproject.generic.GenericSoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ResetTokenRepo extends GenericSoftDeleteRepository<ResetTokenEntity, Long> {
    Optional<ResetTokenEntity> findByUsername(String email);

    @Modifying
    @Query(value = "update reset_token_entity set token=?2 where username= ?1", nativeQuery = true)
    void updateToken(String username,String token);

    @Query(value = "select count(username) from reset_token_entity rte where username =?1",nativeQuery = true)
    int userCount(String username);


}
