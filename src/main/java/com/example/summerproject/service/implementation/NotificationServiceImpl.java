package com.example.summerproject.service.implementation;

import com.example.summerproject.dto.request.NotificationRequestDto;
import com.example.summerproject.entity.NotificationEntity;
import com.example.summerproject.entity.Product;
import com.example.summerproject.mapper.NotificationMapper;
import com.example.summerproject.repo.NotificationRepo;
import com.example.summerproject.repo.ProductRepo;
import com.example.summerproject.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final ProductRepo productRepo;
    private final NotificationRepo notificationRepo;
    private final NotificationMapper notificationMapper;

    //    @Scheduled(cron = "0 30 20 * * *")
    @Scheduled(cron = "0 * * * * *")
    public void popNotification() {
        List<Product> productList = productRepo.findAll();
        productList.forEach(product -> {
            if(product.getStock()<=5){
                notificationRepo.save(NotificationEntity.builder()
                        .title("Product Stock Running low")
                        .description(product.getName()+" stock is "+product.getStock()+ " please take action")
                        .build());
            }
        });
    }

    @Override
    public List<NotificationRequestDto> getAllNotification() {
        return notificationMapper.getAllNotification();
    }

    public boolean markAsRead(Long id){
        notificationRepo.deleteById(id);
        return true;
    }
}
