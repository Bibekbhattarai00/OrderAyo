package com.example.summerproject.controller;


import com.example.summerproject.controller.basecontroller.BaseController;
import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@Tag(name = "Email Controller", description = "APIs for Sending emails to users")
@RequiredArgsConstructor
public class EmailController extends BaseController {

    private final EmailService emailService;

    @Operation(summary = "send mail to users", description = "send mail to specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member found"),
            @ApiResponse(responseCode = "403" ,description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @PostMapping("/send-mail")
    public GenericResponse<String> sendMail(String to, String subject, String body){
        return successResponse(emailService.sendMail(to,subject,body),"mail sent");
    }}
