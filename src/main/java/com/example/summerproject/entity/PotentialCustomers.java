package com.example.summerproject.entity;

import com.example.summerproject.generic.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "tbl_potential_customers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PotentialCustomers implements BaseEntity {
    @Id
    @SequenceGenerator(name = "potential_customers_gen", allocationSize = 1)
    @GeneratedValue(generator = "potential_customers_gen", strategy = GenerationType.SEQUENCE)
    Long id;
    String customerEmail;
    String phone;
    String name;
    @ManyToOne
    @JoinColumn(name = "prod_id")
    Product product;
    @Column(nullable = false, columnDefinition = "boolean default false")
    boolean deleted = Boolean.FALSE;
}
