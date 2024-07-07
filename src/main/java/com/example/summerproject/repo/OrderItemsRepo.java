package com.example.summerproject.repo;

import com.example.summerproject.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepo extends JpaRepository<OrderItem ,Long> {
}
