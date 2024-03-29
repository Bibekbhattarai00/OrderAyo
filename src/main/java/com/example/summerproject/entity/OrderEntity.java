package com.example.summerproject.entity;

import com.example.summerproject.auditing.AuditingEntity;
import com.example.summerproject.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_orders")
public class OrderEntity extends AuditingEntity {
    @Id
    @SequenceGenerator(name = "Order_seq", allocationSize = 1)
    @GeneratedValue(generator = "Order_seq", strategy = GenerationType.SEQUENCE)
    Long id;

    String customerName;

    String customerContact;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems = new ArrayList<>();
}
