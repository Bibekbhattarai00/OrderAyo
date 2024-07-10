package com.example.summerproject.controller;

import com.example.summerproject.controller.basecontroller.BaseController;
import com.example.summerproject.dto.request.NotificationRequestDto;
import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@Tag(name = "notification Controller", description = "APIs for Notifications")
@RequiredArgsConstructor
public class NotificationController extends BaseController {
private final NotificationService notificationService;

    @Operation(summary = " fetches all notifications ", description = "this api fetches all notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "all notification"),
            @ApiResponse(responseCode = "403" ,description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @GetMapping("/get-all")
    public GenericResponse<List<NotificationRequestDto>> fetchNotification(){
        return successResponse(notificationService.getAllNotification(),"Notification fetched");
    }


    @Operation(summary = " mark read ", description = "this api marks notification as read")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "mark as read"),
            @ApiResponse(responseCode = "403" ,description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @GetMapping("/mark-read")
    public GenericResponse<Boolean> markAsRead(@RequestParam Long id){
        return successResponse(notificationService.markAsRead(id),"mark as read");
    }

}
