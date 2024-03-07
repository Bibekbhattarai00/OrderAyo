package com.example.summerproject.securityconfig;

import com.example.summerproject.entity.UserEntity;
import com.example.summerproject.exception.NotFoundException;
import com.example.summerproject.repo.UserEntityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Service
public class UserInfoDetailService implements UserDetailsService {
    private final UserEntityRepo userEntityRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity>user=userEntityRepo.findByUsername(username);
        return user.map(UserInfoDetails::new)
                .orElseThrow(()->new NotFoundException("user Not found"));
    }
}

