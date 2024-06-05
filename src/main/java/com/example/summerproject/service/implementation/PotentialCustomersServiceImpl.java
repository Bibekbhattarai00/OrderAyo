package com.example.summerproject.service.implementation;

import com.example.summerproject.dto.request.PotentialCutomerDto;
import com.example.summerproject.dto.response.PotentialCustomerResponseDto;
import com.example.summerproject.entity.PotentialCustomers;
import com.example.summerproject.entity.Product;
import com.example.summerproject.exception.CustomMessageSource;
import com.example.summerproject.exception.ExceptionMessages;
import com.example.summerproject.exception.NotFoundException;
import com.example.summerproject.repo.PotentialCustomerRepo;
import com.example.summerproject.repo.ProductRepo;
import com.example.summerproject.service.PotentialCustomersService;
import com.example.summerproject.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PotentialCustomersServiceImpl implements PotentialCustomersService {
    private final PotentialCustomerRepo potentialCustomerRepo;
    private final ProductRepo productRepo;
    private final CustomMessageSource messageSource;
    private final MailUtils mailUtils;

    @Override
    public boolean addCustomer(PotentialCutomerDto potentialCutomerDto) {
        Product product = productRepo.findById(potentialCutomerDto.getProductId()).orElseThrow(() -> new NotFoundException(messageSource.get(ExceptionMessages.NOT_FOUND.getCode())));
        potentialCustomerRepo.save(PotentialCustomers.builder()
                .name(potentialCutomerDto.getName())
                .phone(potentialCutomerDto.getPhone())
                .product(product)
                .customerEmail(potentialCutomerDto.getCustomerEmail())
                .build());
        return true;
    }

//    @Scheduled(fixedRate = 500)
 @Scheduled(cron = "0 30 20 * * *")
    @Override
    public void notifyCustomers() {
        List<PotentialCustomers> potentialCustomerRepoAll = potentialCustomerRepo.findAll();
        for (PotentialCustomers potentialCustomers : potentialCustomerRepoAll) {
            Product product = potentialCustomers.getProduct();
            if (product.getStock() > 0) {
                String body = "Dear customer \n" + potentialCustomers.getName() +
                        " your enquiry for " + product.getName() + " on regina shoe store" +
                        " has been re-stocked please contact to the Service providers \n" +
                        "contact:9800000000 \n" +
                        "Thank you";
                String subject = "sub:- product restocked notification ";
                mailUtils.sendMail(potentialCustomers.getCustomerEmail(), subject, body);
            }
            potentialCustomerRepo.delete(potentialCustomers);
        }
    }

    @Override
    public List<PotentialCustomerResponseDto> getPotentialCustomers() {

        return null;
    }
}
