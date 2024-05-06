package com.example.summerproject.mapper;

import com.example.summerproject.dto.response.OrderResponseDto;
import com.example.summerproject.dto.response.ProdnameResponseDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Select(" SELECT DISTINCT to2.id as orderId, " +
            "   to2.customer_name AS customerName, " +
            "   to2.customer_contact AS customerContact, " +
            "   SUM(tp.selling_price * op.quantity) OVER (PARTITION BY to2.id) AS total ," +
            " to2.created_date as orderDate " +
            " FROM " +
            "   tbl_orders to2 " +
            "INNER JOIN " +
            "   order_products op ON to2.id = op.order_id " +
            "INNER JOIN " +
            "   tbl_products tp ON tp.prod_id = op.product_id " +
            "WHERE to2.deleted =false and " +
            "   to2.order_status = 'PENDING'")
    @Results({
            @Result(property = "orderId", column = "orderId"),
            @Result(property = "customerName", column = "customerName"),
            @Result(property = "customerContact", column = "customerContact"),
            @Result(property = "products", column = "orderId", javaType = List.class, many = @Many(select = "getProductsForOrder")),
            @Result(property = "total", column = "total"),
    })
    List<OrderResponseDto> getOrdersWithProducts();

    @Select("SELECT DISTINCT " +
            "tp.prod_id as prodId , " +
            "   tp.name AS productName, " +
            "   tp.prod_type AS productType, " +
            "   op.quantity AS quantity, " +
            "   tp.selling_price * op.quantity AS price " +
            "FROM " +
            "   tbl_products tp " +
            "INNER JOIN " +
            "   order_products op ON tp.prod_id = op.product_id " +
            "WHERE " +
            "   op.order_id = #{orderId}")
    List<ProdnameResponseDto> getProductsForOrder(@Param("orderId") Long orderId);



    @Select("SELECT DISTINCT to2.id as orderId, " +
            "   to2.customer_name AS customerName, " +
            "   to2.customer_contact AS customerContact, " +
            "   SUM(tp.selling_price * op.quantity) OVER (PARTITION BY to2.id) AS total " +
            "FROM " +
            "   tbl_orders to2 " +
            "INNER JOIN " +
            "   order_products op ON to2.id = op.order_id " +
            "INNER JOIN " +
            "   tbl_products tp ON tp.prod_id = op.product_id " +
            "WHERE to2.deleted =false and " +
            "   to2.customer_contact = #{customerContact} " +
            "   AND to2.order_status = 'PENDING'")
    @Results({
            @Result(property = "orderId", column = "orderId"),
            @Result(property = "customerName", column = "customerName"),
            @Result(property = "customerContact", column = "customerContact"),
            @Result(property = "products", column = "orderId", javaType = List.class, many = @Many(select = "getProductsForOrder")),
            @Result(property = "total", column = "total"),
    })
    List<OrderResponseDto> getOrdersWithProductsByCustomer(@Param("customerContact") String customerContact);


    @Select("SELECT DISTINCT to2.id as orderId, " +
            "   to2.customer_name AS customerName, " +
            "   to2.customer_contact AS customerContact, " +
            "   SUM(tp.selling_price * op.quantity) OVER (PARTITION BY to2.id) AS total " +
            "FROM " +
            "   tbl_orders to2 " +
            "INNER JOIN " +
            "   order_products op ON to2.id = op.order_id " +
            "INNER JOIN " +
            "   tbl_products tp ON tp.prod_id = op.product_id " +
            "WHERE to2.deleted =false and " +
            "   to2.id = #{OrderId} " +
            "   AND to2.order_status = 'PENDING'")
    @Results({
            @Result(property = "orderId", column = "orderId"),
            @Result(property = "customerName", column = "customerName"),
            @Result(property = "customerContact", column = "customerContact"),
            @Result(property = "products", column = "orderId", javaType = List.class, many = @Many(select = "getProductsForOrder")),
            @Result(property = "total", column = "total"),
    })
    OrderResponseDto getOrderDetailsById(@Param("OrderId") Long orderId);


    @Select(" SELECT DISTINCT to2.id as orderId, to2.created_date as orderDate," +
            "   to2.customer_name AS customerName, " +
            "   to2.customer_contact AS customerContact, " +
            "   SUM(tp.selling_price * op.quantity) OVER (PARTITION BY to2.id) AS total " +
            "FROM " +
            "   tbl_orders to2 " +
            "INNER JOIN " +
            "   order_products op ON to2.id = op.order_id " +
            "INNER JOIN " +
            "   tbl_products tp ON tp.prod_id = op.product_id " +
            "WHERE to2.deleted =true and" +
            "   to2.order_status = 'DISPATCHED'")
    @Results({
            @Result(property = "orderId", column = "orderId"),
            @Result(property = "customerName", column = "customerName"),
            @Result(property = "customerContact", column = "customerContact"),
            @Result(property = "products", column = "orderId", javaType = List.class, many = @Many(select = "getProductsForOrder")),
            @Result(property = "total", column = "total"),
    })
    List<OrderResponseDto> getOrderHistory();
}

