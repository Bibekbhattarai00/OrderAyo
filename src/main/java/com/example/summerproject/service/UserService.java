package com.example.summerproject.service;

import com.example.summerproject.dto.request.UserDto;
import com.example.summerproject.dto.response.UserResponseDto;
import com.example.summerproject.entity.UserEntity;

import java.util.List;



public interface UserService {
    public String addUser(UserDto userDto);
    public List<UserEntity> getUser();
    public String deleteUser(Long id);
    public UserResponseDto getUserById(String username);
}
