package com.example.summerproject.entity;

import com.example.summerproject.auditing.AuditingEntity;
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
@Table(name = "order_products")
public class OrderItem extends AuditingEntity {
    @Id
    @SequenceGenerator(name = "OrderItem_seq", allocationSize = 1)
    @GeneratedValue(generator = "OrderItem_seq", strategy = GenerationType.SEQUENCE)
    Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    Long quantity;
    boolean deleted= Boolean.FALSE;
}