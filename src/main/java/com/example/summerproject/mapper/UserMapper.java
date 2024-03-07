package com.example.summerproject.mapper;


import com.example.summerproject.dto.response.UserResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select tu.username as Email ,tu.modified_by as AddedBy , tu.user_type as Role \n" +
            "from tbl_user tu where username =#{username}")
    UserResponseDto getUser(String username);

}
