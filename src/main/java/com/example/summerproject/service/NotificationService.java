package com.example.summerproject.service;

import com.example.summerproject.dto.request.NotificationRequestDto;

import java.util.List;

public interface NotificationService {
    public void popNotification();
    public List<NotificationRequestDto> getAllNotification();
    public boolean markAsRead(Long id);
}
