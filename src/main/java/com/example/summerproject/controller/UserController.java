package com.example.summerproject.controller;

import com.example.summerproject.controller.basecontroller.BaseController;
import com.example.summerproject.dto.request.AuthenticationDto;
import com.example.summerproject.dto.request.UserDto;
import com.example.summerproject.entity.UserEntity;
import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.exception.NotFoundException;
import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.jwt.JwtService;
import com.example.summerproject.repo.UserEntityRepo;
import com.example.summerproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/user")
@RequiredArgsConstructor
@RestController
@Tag(name = "User controller", description = "Manages APIs for Login, Creating user,Reset password,Deactivate account")
public class UserController extends BaseController {
    private final UserService userService;
    private final CustomMessageSource messageSource;
    private final JwtService jwtService;
    private final UserEntityRepo userEntityRepo;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Add Users", description = "Add users and provide them Roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User added"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add-user")
    public GenericResponse<String> addUser(@RequestBody UserDto userEntityDto) {
        return successResponse(userService.addUser(userEntityDto), "New user " + userEntityDto.getUserType() + " Added");
    }

    @Operation(summary = "Login", description = "User login ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Logged in"),
            @ApiResponse(responseCode = "404", description = "User Not found"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping("/login")
    public GenericResponse<String> login(@RequestBody AuthenticationDto authenticationDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDto.getUsername(), authenticationDto.getPassword()));
        if (authentication.isAuthenticated()) {
            UserEntity byUsername = userEntityRepo.findByUsername(authentication.getName())
                    .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
            if (byUsername.isDeleted()) {
                throw new NotFoundException(messageSource.get(ExceptionMessages.INVALID_CREDENTIALS.getCode()));
            }
            String role = String.valueOf(byUsername.getUserType());
            return successResponse(jwtService.generateToken(authenticationDto.getUsername(), role), messageSource.get(ExceptionMessages.SUCCESS.getCode()));

        } else {
            return errorResponse(messageSource.get(ExceptionMessages.INVALID_CREDENTIALS.getCode()));
        }
    }
}
