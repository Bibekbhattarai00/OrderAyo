package com.example.summerproject.mapper;


import com.example.summerproject.dto.response.UserResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select tu.user_id , tu.username as Email ,tu.modified_by as AddedBy , tu.user_type as Role \n" +
            "from tbl_user tu where tu.deleted= false and tu.username =#{username}")
    UserResponseDto getUser(String username);


    @Select("select tu.user_id as id, tu.username as Email ,tu.modified_by as AddedBy , tu.deleted as deleted ,tu.user_type as Role \n" +
            "from tbl_user tu ")
    List<UserResponseDto> getAllUser();
}
