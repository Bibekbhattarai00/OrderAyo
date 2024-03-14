package com.example.summerproject.service;

import com.example.summerproject.dto.request.OrderDto;
import com.example.summerproject.dto.response.OrderResponseDto;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface OrdersService {

    public String placeOrder(OrderDto orderDto);
    public List<OrderResponseDto> viewPendingOrders();
    public List<OrderResponseDto> viewOrderHistory();
    public String dispatchOrder(Long id);
    public List<OrderResponseDto> viewOrderByCustomer(String phone);
    public OrderResponseDto getOrderDetailsById(Long phone);

    public String generateBill(Long orderId, HttpServletResponse response) throws IOException, DocumentException;
    public String getExcel(HttpServletResponse response) throws IOException, IllegalAccessException;

}
