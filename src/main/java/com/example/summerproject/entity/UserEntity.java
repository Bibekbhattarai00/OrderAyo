package com.example.summerproject.entity;


import com.example.summerproject.auditing.AuditingEntity;
import com.example.summerproject.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_user")
public class UserEntity extends AuditingEntity {
    @Id
    @SequenceGenerator(name = "user_key" ,allocationSize = 1)
    @GeneratedValue(generator = "user_key" ,strategy = GenerationType.SEQUENCE)
    @Column(name="user_id")
    Long id;

    @Column(unique = true, columnDefinition = "citext")
    String username;

    String password;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    UserType userType;
    boolean deleted= Boolean.FALSE;
}
