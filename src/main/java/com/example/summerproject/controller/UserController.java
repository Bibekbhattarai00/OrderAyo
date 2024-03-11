package com.example.summerproject.controller;

import com.example.summerproject.controller.basecontroller.BaseController;
import com.example.summerproject.dto.request.AuthenticationDto;
import com.example.summerproject.dto.request.PasswordResetDto;
import com.example.summerproject.dto.request.UserDto;
import com.example.summerproject.dto.response.UserResponseDto;
import com.example.summerproject.entity.UserEntity;
import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.exception.NotFoundException;
import com.example.summerproject.geneericresponse.GenericResponse;
import com.example.summerproject.jwt.JwtService;
import com.example.summerproject.repo.UserEntityRepo;
import com.example.summerproject.service.PasswordResetService;
import com.example.summerproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.hibernate.dialect.LobMergeStrategy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final PasswordResetService passwordResetService;

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
    @Operation(summary = "Reset User's password" ,description = "reset users password based on authentication token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "Password reset Success"),
            @ApiResponse(responseCode = "404" ,description = "User Not found"),
            @ApiResponse(responseCode = "500" ,description = "internal server error"),
            @ApiResponse(responseCode = "403" ,description = "Forbidden"),
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    @PostMapping("/reset")
    public GenericResponse<String> reset(HttpServletRequest request, @RequestBody PasswordResetDto passwordResetDto) {
        return GenericResponse.<String>builder()
                .success(true)
                .message("password changed")
                .data(passwordResetService.requestReset(request, passwordResetDto))
                .build();
    }

    @Operation(summary = "get Users", description = "get users registered in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users fetched"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/get-user")
    public GenericResponse<List<UserResponseDto>> getUser() {
        return successResponse(userService.getUser(), messageSource.get(ExceptionMessages.SUCCESS.getCode()));
    }


    @Operation(summary = "get User by username", description = "get users registered in the system by its username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User fetched"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/get-user-by-username")
    public GenericResponse<UserResponseDto> getUser(String username) {
        return successResponse(userService.getUserById(username), messageSource.get(ExceptionMessages.SUCCESS.getCode()));
    }

    @Operation(summary = "get User by username and deactivate its status", description = "get users registered in the system by its username and deactivate its status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deactivated"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deactivate")
    public GenericResponse<String> deactivateUser(Long id) {
        return successResponse(userService.deleteUser(id), messageSource.get(ExceptionMessages.SUCCESS.getCode()));
    }

}
