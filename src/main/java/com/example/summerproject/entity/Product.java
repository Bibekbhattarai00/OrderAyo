package com.example.summerproject.entity;

import com.example.summerproject.auditing.AuditingEntity;
import com.example.summerproject.enums.ProdType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "tbl_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AuditingEntity {
    @Id
    @SequenceGenerator(name = "product_key", allocationSize = 1)
    @GeneratedValue(generator = "product_key", strategy = GenerationType.SEQUENCE)
    @Column(name = "prod_id")
    Long id;

    @Column(name = "name", unique = true, columnDefinition = "citext")
    String name;

    @Column(name = "stock")
    Long stock;

    @Enumerated(EnumType.STRING)
    ProdType prodType;

    Long sellingPrice;

    Long costPrice;
    String image;

    boolean deleted = Boolean.FALSE;
}
