package com.example.summerproject.mapper;

import com.example.summerproject.dto.request.NotificationRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Select("select n.id,n.description,n.title, to_char(n.created_date,'yyyy-MM-dd:hh:mm:ss') as createdDate , n.deleted as status" +
            " from noficication n ")
    List<NotificationRequestDto> getAllNotification();

}
