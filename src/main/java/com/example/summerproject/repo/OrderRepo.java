package com.example.summerproject.repo;

import com.example.summerproject.entity.OrderEntity;
import com.example.summerproject.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<OrderEntity , Long> {

    Optional<List<OrderEntity>> findByOrderStatus(OrderStatus orderStatus);
}
