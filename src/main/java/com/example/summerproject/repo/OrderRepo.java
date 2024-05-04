package com.example.summerproject.repo;

import com.example.summerproject.entity.OrderEntity;
import com.example.summerproject.enums.OrderStatus;
import com.example.summerproject.generic.repo.GenericSoftDeleteRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface OrderRepo extends GenericSoftDeleteRepository<OrderEntity , Long> {

    Optional<List<OrderEntity>> findByOrderStatus(OrderStatus orderStatus);

    @Query(value = "SELECT \n" +
            "    SUM(CASE WHEN to2.order_status = 'DISPATCHED' THEN tp.selling_price ELSE 0 END) AS shipped_orders,\n" +
            "    SUM(CASE WHEN to2.order_status = 'PENDING' THEN tp.selling_price ELSE 0 END) AS active_orders,\n" +
            "    SUM(tp.selling_price) AS total_orders\n" +
            "FROM \n" +
            "    order_products op \n" +
            "    JOIN tbl_products tp ON tp.prod_id = op.product_id \n" +
            "    JOIN tbl_orders to2 ON to2.id = op.order_id\n" +
            "    where  to2.created_date BETWEEN COALESCE(?1, to2.created_date) AND COALESCE(?2, to2.created_date)" ,nativeQuery = true)
    Map<String, Object> getSalesReport(Date fromDate,Date toDate);

    @Query(value = "SELECT \n" +
            "    op.product_id,\n" +
            "    COUNT(op.order_id) AS order_count\n" +
            "FROM \n" +
            "    order_products op \n" +
            " where  op.created_date BETWEEN COALESCE(?1, op.created_date) AND COALESCE(?2, op.created_date) " +
            "GROUP BY \n" +
            "    op.product_id\n" +
            "ORDER BY \n" +
            "    order_count DESC\n" +
            "LIMIT 5;" ,nativeQuery = true)
    Map<String, Object> getBestSeller(Date fromDate,Date toDate);
}
