package com.example.summerproject.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface GenericRepository<T extends BaseEntity, ID extends Serializable> extends
        JpaSpecificationExecutor<T>,
        JpaRepository<T, ID> {

}
