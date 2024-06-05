package com.example.summerproject.controller;


import com.example.summerproject.controller.basecontroller.BaseController;
import com.example.summerproject.dto.request.DateRequestDto;
import com.example.summerproject.dto.request.OrderDto;
import com.example.summerproject.dto.response.OrderResponseDto;
import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.service.OrdersService;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


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
    @GetMapping("/get-orders-history")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<List<OrderResponseDto>>  viewOrderHistory(){
        return successResponse(ordersService.viewOrderHistory(),"All orders history");
    }

    @Operation(summary = "View orders", description = "view pending orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pending orders fetched"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/get-orders")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<List<OrderResponseDto>>  viewPendingOrders(){
        return successResponse(ordersService.viewPendingOrders(),"All pending orders");
    }

    @Operation(summary = "View orders by id", description = "view pending orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pending orders fetched"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/get-orders-by-id")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<OrderResponseDto>  viewPendingOrders(@RequestParam Long id){
        return successResponse(ordersService.viewPendingOrdersById(id),"pending orders by id");
    }

    @Operation(summary = "Dispatch order orders", description = "Dispatch orders by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "dispatch Success"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @DeleteMapping("/dispatch-orders")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<String>  dispatchOrdersById(@RequestParam Long id){
        return successResponse(ordersService.dispatchOrder(id),"dispatch orders");
    }

    @Operation(summary = "View orders by PhoneNo", description = "view pending orders by customer phoneNo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pending orders fetched"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/get-orders-byPhone")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<List<OrderResponseDto>>  viewPendingOrders(@RequestParam String phone){
        return successResponse(ordersService.viewOrderByCustomer(phone),"All pending orders of the customer " + phone);
    }

    @Operation(summary = "Get bill", description = "get bill and print bill")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "getBill and print bill"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/get-bill")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<String>  generateBills(@RequestParam Long id, HttpServletResponse response) throws IOException, DocumentException {
        return successResponse(ordersService.generateBill(id,response),"All pending orders of the customer " + id);
    }

    @Operation(summary = "generate excel", description = "generate excel sheet of order History")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "generate excel sheet of order History"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/download-history")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<String> getExcel(HttpServletResponse response) throws IOException, IllegalAccessException {
        return successResponse(ordersService.getExcel(response),"excelSheet downloaded");
    }

    @Operation(summary = "get sales Report", description = "gat report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pending orders fetched"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping("/get-sales-report")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<Map<String,Object>>  viewPendingOrders(@RequestBody DateRequestDto dateRequestDto){
        return successResponse(ordersService.getSalesReport(dateRequestDto),"sales report");
    }


    @Operation(summary = "get best sellers", description = "get best sellers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pending orders fetched"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping("/get-best-sellers")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<List<Map<String,Object>>>  getBestSeller(@RequestBody DateRequestDto dateRequestDto){
        return successResponse(ordersService.getBestSellers(dateRequestDto),"best sellers");
    }


}
