package com.example.summerproject.entity;

import com.example.summerproject.auditing.AuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetTokenEntity extends AuditingEntity {
    @Id
    @SequenceGenerator(name = "resetToken_primary_key_generator", initialValue = 0, allocationSize = 1,sequenceName = "resetToken_primary_key_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resetToken_primary_key_generator")
    Long id;
    String username;
    String token;
}
