package com.example.summerproject.entity;


import com.example.summerproject.auditing.AuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_user")
public class UserEntity extends AuditingEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "user_key" ,allocationSize = 1)
    @GeneratedValue(generator = "user_key" ,strategy = GenerationType.SEQUENCE)
    @Column(name="user_id")
    Long userId;

    String username;

    String password;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    UserType userType;

    boolean deleted=false;

}
