package com.example.summerproject.service.implementation;

import ch.qos.logback.classic.Logger;
import com.example.summerproject.dto.request.OrderDto;
import com.example.summerproject.dto.request.OrderItemDto;
import com.example.summerproject.dto.response.OrderResponseDto;
import com.example.summerproject.entity.OrderEntity;
import com.example.summerproject.entity.OrderItem;
import com.example.summerproject.entity.Product;
import com.example.summerproject.enums.OrderStatus;
import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.exception.NotFoundException;
import com.example.summerproject.mapper.OrderMapper;
import com.example.summerproject.repo.OrderRepo;
import com.example.summerproject.repo.ProductRepo;
import com.example.summerproject.service.OrdersService;
import com.example.summerproject.utils.MailUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrdersService {

    private final OrderRepo orderRepo;
    private final ObjectMapper objectMapper;
    private final ProductRepo productRepo;
    private final CustomMessageSource messageSource;
    private final OrderMapper orderMapper;
    private final MailUtils mailUtils;

    @Override
    public String placeOrder(OrderDto orderDto) {
        List<OrderItemDto> orderItemDtos = orderDto.getOrderItems();
        List<OrderItem> orderItems = new ArrayList<>();

        OrderEntity order = new OrderEntity();
        order.setCustomerName(orderDto.getCustomerName());
        order.setCustomerContact(orderDto.getCustomerContact());
        order.setOrderStatus(OrderStatus.PENDING);

        for (OrderItemDto orderItemDto : orderItemDtos) {
            Long productId = orderItemDto.getProductId();
            Long quantity = orderItemDto.getQuantity();

            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));

            if (product.isDeleted() || product.getStock() < quantity) {
                throw new NotFoundException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - quantity);
            productRepo.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        orderRepo.save(order);

        return messageSource.get(ExceptionMessages.SUCCESS.getCode());
    }

    @Override
    public List<OrderResponseDto> viewPendingOrders() {
        return orderMapper.getOrdersWithProducts();
    }

    @Override
    public List<OrderResponseDto> viewOrderHistory() {
        return orderMapper.getOrderHistory();
    }

    @Override
    public String dispatchOrder(Long id) {
        OrderEntity order = orderRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
        order.setOrderStatus(OrderStatus.DISPATCHED);
        orderRepo.save(order);
        return messageSource.get(ExceptionMessages.SUCCESS.getCode());
    }

    @Override
    public List<OrderResponseDto> viewOrderByCustomer(String phone) {
        return orderMapper.getOrdersWithProductsByCustomer(phone);
    }


    @Scheduled(cron = "0 30 20 * * *")

//    @Scheduled(cron = "0 45 23 * * *")
//    @Scheduled(fixedRate = 500)
    public void sendPendingOrderReminderEmail() {
        Optional<List<OrderEntity>> pendingOrders = orderRepo.findByOrderStatus(OrderStatus.PENDING);

        if (pendingOrders.isPresent()) {
            List<OrderEntity> orders = pendingOrders.get();

            for (OrderEntity order : orders) {
                Date createdDate = order.getCreatedDate();
                if (createdDate != null && is24HoursPassed(createdDate) && order.getOrderStatus() == OrderStatus.PENDING) {
                    String emailBody = "Your order created on " + createdDate + " is still pending. Please take action.";
                    mailUtils.sendMail("bibek@yopmail.com", "Pending Order Reminder", emailBody);
                }
            }

        } else {
            System.out.println("No pending orders");
        }
    }
private boolean is24HoursPassed(Date createdDate) {
    LocalDateTime createdDateTime = createdDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    LocalDateTime now = LocalDateTime.now();
    return Duration.between(createdDateTime, now).toHours() > 24;
}

}
