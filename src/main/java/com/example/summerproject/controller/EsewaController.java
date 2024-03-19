package com.example.summerproject.controller;

import com.example.summerproject.controller.basecontroller.BaseController;
import com.example.summerproject.dto.request.EsewaRequestDto;
import com.example.summerproject.dto.request.PaymentRequestDto;
import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.service.EsewaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/Esewa")
@Tag(name = "Esewa Controller", description = "payment")
@RequiredArgsConstructor
public class EsewaController extends BaseController {

    private final EsewaService esewaService;

    @Operation(summary = "Test", description = "Esewa Test")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "403" ,description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @PostMapping("/send-req")
    public GenericResponse<ResponseEntity<String>> sendReq(@RequestBody PaymentRequestDto paymentRequestDto){
        EsewaRequestDto esewaRequestDto1 = esewaService.requestPayment(paymentRequestDto);
        return successResponse(esewaService.paymentRequest(esewaRequestDto1),"Req sent");
    }


}
