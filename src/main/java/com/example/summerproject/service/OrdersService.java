package com.example.summerproject.service;

import com.example.summerproject.dto.request.OrderDto;
import com.example.summerproject.dto.response.OrderResponseDto;

import java.util.List;

public interface OrdersService {

    public String placeOrder(OrderDto orderDto);
    public List<OrderResponseDto> viewPendingOrders();
    public List<OrderResponseDto> viewOrderHistory();
    public String dispatchOrder(Long id);
    public List<OrderResponseDto> viewOrderByCustomer(String phone);

}
