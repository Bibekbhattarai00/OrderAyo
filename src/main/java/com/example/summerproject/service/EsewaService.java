package com.example.summerproject.service;

import com.example.summerproject.dto.request.EsewaRequestDto;
import com.example.summerproject.dto.request.PaymentRequestDto;
import org.springframework.http.ResponseEntity;


public interface EsewaService {
    EsewaRequestDto requestPayment(PaymentRequestDto paymentRequestDto);

    public ResponseEntity<String> paymentRequest(EsewaRequestDto esewaRequestDto);
}
