package com.example.summerproject.service.implementation;

import com.example.summerproject.dto.request.EsewaRequestDto;
import com.example.summerproject.dto.request.PaymentRequestDto;
import com.example.summerproject.service.EsewaService;
import com.example.summerproject.utils.EsewaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EsewaServiceimpl implements EsewaService {

    private final EsewaUtil util;
    private final RestTemplate restTemplate;

    @Override
    public EsewaRequestDto requestPayment(PaymentRequestDto paymentRequestDto) {
        return EsewaRequestDto.builder()
                .amount(paymentRequestDto.getAmount())
                .productCode("EPAYTEST")
                .productDeliveryCharge(util.calculateDeliveryCharge(paymentRequestDto.getAmount()))
                .productServiceCharge(util.calculateServiceCharge(paymentRequestDto.getAmount()))
                .failureUrl("https://google.com")
                .signedFieldNames(paymentRequestDto.getAmount()+","+util.generateTransactionUuid()+",EPAYTEST")
                .successUrl("https://esewa.com.np")
                .taxAmount(util.calculateTax(paymentRequestDto.getAmount()))
                .totalAmount(util.calculateTotal(paymentRequestDto.getAmount()))
                .signature(util.Esewahash(util.calculateTotal(paymentRequestDto.getAmount()),util.generateTransactionUuid()))
                .transactionUuid(util.generateTransactionUuid())
                .build();
    }

    public ResponseEntity<String> paymentRequest(EsewaRequestDto esewaRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("amount", esewaRequestDto.getAmount());
        formData.add("productCode", esewaRequestDto.getProductCode());
        formData.add("productDeliveryCharge", esewaRequestDto.getProductDeliveryCharge());
        formData.add("productServiceCharge", esewaRequestDto.getProductServiceCharge());
        formData.add("failureUrl", esewaRequestDto.getFailureUrl());
        formData.add("successUrl", esewaRequestDto.getSuccessUrl());
        formData.add("taxAmount", esewaRequestDto.getTaxAmount());
        formData.add("totalAmount", esewaRequestDto.getTotalAmount());
        formData.add("signature", util.Esewahash(esewaRequestDto.getTotalAmount(), esewaRequestDto.getTransactionUuid()));
        formData.add("transactionUuid", esewaRequestDto.getTransactionUuid());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        return restTemplate.postForEntity(
                "https://rc-epay.esewa.com.np/api/epay/main/v2/form",
                requestEntity,
                String.class);
    }

}