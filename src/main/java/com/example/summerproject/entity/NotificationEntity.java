package com.example.summerproject.entity;

import com.example.summerproject.auditing.AuditingEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "noficication")
@Builder
public class NotificationEntity extends AuditingEntity {
    @Id
    @SequenceGenerator(name = "notification_seq", allocationSize = 1)
    @GeneratedValue(generator = "notification_seq", strategy = GenerationType.SEQUENCE)
    Long id;
    String title;
    String description;
}
