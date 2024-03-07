package com.example.summerproject.service.implementation;

import com.example.summerproject.dto.request.UserDto;
import com.example.summerproject.entity.UserEntity;
import com.example.summerproject.repo.UserEntityRepo;
import com.example.summerproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ObjectMapper objectMapper;
    private final UserEntityRepo userRepo;
    private final PasswordEncoder encoder;

    @Override
    public String addUser(UserDto userDto) {
        UserEntity user = objectMapper.convertValue(userDto, UserEntity.class);
        user.setPassword(encoder.encode(userDto.getPassword()));
        userRepo.save(user);
        return "user added";
    }

    @Override
    public List<UserEntity> getUser() {
        return userRepo.findAll();
    }

    @Override
    public String deleteUser(Long id) {
        UserEntity user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("userNot found"));
        userRepo.deleteById(id);
        return "user deleted "+ user.getUserId();
    }

    @Override
    public UserEntity getUserById(Long id) {
        UserEntity user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("userNot found"));
        return user;
    }
}
