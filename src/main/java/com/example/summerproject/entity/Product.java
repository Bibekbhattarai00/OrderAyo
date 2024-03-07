package com.example.summerproject.entity;

import com.example.summerproject.auditing.AuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;


@Entity
@Table(name = "tbl_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AuditingEntity implements Serializable  {
    @Id
    @SequenceGenerator(name = "product_key" ,allocationSize = 100)
    @GeneratedValue(generator = "product_key" ,strategy = GenerationType.SEQUENCE)
            @Column(name="prod_id")
    Long prodId;

    @Column(name = "name")
    String name;

    @Column(name = "stock")
    Long stock;

    @Enumerated(EnumType.STRING)
    ProdType prodType;

    Long selling_price;

    Long cost_price;

    boolean deleted= false;
}
