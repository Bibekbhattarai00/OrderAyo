package com.example.summerproject.service;

import com.example.summerproject.dto.request.UserDto;
import com.example.summerproject.dto.response.UserResponseDto;
import java.util.List;



public interface UserService {
    public String addUser(UserDto userDto);
    public List<UserResponseDto> getUser();
    public String deleteUser(Long id);
    public UserResponseDto getUserById(String username);
}
