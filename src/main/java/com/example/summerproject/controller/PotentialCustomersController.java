package com.example.summerproject.controller;

import com.example.summerproject.controller.basecontroller.BaseController;
import com.example.summerproject.dto.request.PotentialCutomerDto;
import com.example.summerproject.dto.request.ProductDto;
import com.example.summerproject.dto.response.PotentialCustomerResponseDto;
import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.service.PotentialCustomersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/potential-customers")
@RequiredArgsConstructor
public class PotentialCustomersController extends BaseController {

    private final PotentialCustomersService potentialCustomersService;

    @Operation(summary = "Add potential Customers", description = "Add potential Customers to the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "potential Customers added"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<Boolean> addCustomer(@RequestBody PotentialCutomerDto potentialCutomerDto) {
        return successResponse(potentialCustomersService.addCustomer(potentialCutomerDto), "Potential Customers added added");
    }

    @Operation(summary = "Add potential Customers", description = "Add potential Customers to the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "potential Customers added"),
            @ApiResponse(responseCode = "500", description = "internal server error")
    })
    @GetMapping("/get-potential-customers")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public GenericResponse<List<PotentialCustomerResponseDto>> getCustomers() {
        return successResponse(potentialCustomersService.getPotentialCustomers(), "Potential Customers retrieved");
    }

}
