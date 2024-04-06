package com.example.summerproject.auditing;

import com.example.summerproject.generic.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AuditingEntity implements BaseEntity {
    @CreatedDate
    @Temporal(TIMESTAMP)
    @JsonFormat(pattern = "yyyy-mm-dd")
    Date createdDate;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-mm-dd")
    @Temporal(TIMESTAMP)
    Date modifiedDate;

    @LastModifiedBy
    String modifiedBy;

    @CreatedBy
    String createdBy;


}
