package com.example.summerproject.controller;


import com.example.summerproject.controller.basecontroller.BaseController;
import com.example.summerproject.dto.request.OrderDto;
import com.example.summerproject.dto.request.ProductDto;
import com.example.summerproject.dto.response.OrderResponseDto;
import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController extends BaseController {
    private final OrdersService ordersService;

    @Operation(summary = "Place order", description = "place Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order placed"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping("/add-order")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<String> addAuthor(@RequestBody @Valid OrderDto orderDto) {
        return successResponse(ordersService.placeOrder(orderDto), "Product added");
    }

    @Operation(summary = "View orders history", description = "view orders history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pending orders fetched"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping("/get-orders-history")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<List<OrderResponseDto>>  viewOrderHistory(){
        return successResponse(ordersService.viewOrderHistory(),"All orders history");
    }

    @Operation(summary = "View orders", description = "view pending orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pending orders fetched"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping("/get-orders")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<List<OrderResponseDto>>  viewPendingOrders(){
        return successResponse(ordersService.viewPendingOrders(),"All pending orders");
    }


    @Operation(summary = "View orders by PhoneNo", description = "view pending orders by customer phoneNo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pending orders fetched"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping("/get-orders-byPhone")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<List<OrderResponseDto>>  viewPendingOrders(@RequestParam String phone){
        return successResponse(ordersService.viewOrderByCustomer(phone),"All pending orders of the customer " + phone);
    }


}
