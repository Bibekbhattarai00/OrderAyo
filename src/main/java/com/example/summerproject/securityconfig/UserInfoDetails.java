package com.example.summerproject.securityconfig;

import com.example.summerproject.entity.UserEntity;
import com.example.summerproject.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoDetails implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;


    public UserInfoDetails(UserEntity userEntity) {
        username = userEntity.getUsername();
        password = userEntity.getPassword();
        authorities = Arrays.stream(UserType.values())
                //When creating the authorities list to include only the roles
                // that correspond to the USER_TYPE of the authenticated user
                //The filter operation ensures that only the USER_TYPE value
                // matching the userType of the authenticated user are retained in the stream
                // and mapped to simpleGranted authority
                .filter(userType -> userEntity.getUserType() == userType)
                .map(userType -> new SimpleGrantedAuthority("ROLE_" + userType.name()))
                .collect(Collectors.toList());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
