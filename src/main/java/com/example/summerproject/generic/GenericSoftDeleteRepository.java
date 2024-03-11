package com.example.summerproject.generic;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface GenericSoftDeleteRepository<T extends BaseEntity, ID extends Serializable> extends
        GenericRepository<T, ID> {

}
