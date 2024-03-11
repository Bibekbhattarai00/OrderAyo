package com.example.summerproject.service.implementation;

import com.example.summerproject.dto.request.UserDto;
import com.example.summerproject.dto.response.UserResponseDto;
import com.example.summerproject.entity.UserEntity;
import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.exception.NotFoundException;
import com.example.summerproject.mapper.UserMapper;
import com.example.summerproject.repo.UserEntityRepo;
import com.example.summerproject.service.UserService;
import com.example.summerproject.utils.GetUserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ObjectMapper objectMapper;
    private final UserEntityRepo userRepo;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final CustomMessageSource  messageSource;


    @Override
    public String addUser(UserDto userDto) {
        UserEntity user = objectMapper.convertValue(userDto, UserEntity.class);
        user.setPassword(encoder.encode(userDto.getPassword()));
        userRepo.save(user);
        return messageSource.get(ExceptionMessages.SUCCESS.getCode());
    }

    @Override
    public List<UserResponseDto> getUser() {
        GetUserRole.checkAuthority();
        return userMapper.getAllUser();
    }

    @Override
    public String deleteUser(Long id) {
        GetUserRole.checkAuthority();
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
        user.setDeleted(true);
        userRepo.save(user);
        return messageSource.get(ExceptionMessages.DELETED.getCode())+" " + user.getUsername();
    }

    @Override
    public UserResponseDto getUserById(String username) {
        GetUserRole.checkAuthority();
        UserResponseDto userResponseDto = userMapper.getUser(username);
        if (userResponseDto != null) {
            return userResponseDto;
        } else {
            throw new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode()));
        }
    }
}
