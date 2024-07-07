package com.example.summerproject.service;

import com.example.summerproject.dto.request.PotentialCutomerDto;
import com.example.summerproject.dto.response.PotentialCustomerResponseDto;

import java.util.List;

public interface PotentialCustomersService {

    public boolean addCustomer(PotentialCutomerDto potentialCutomerDto);
    public void notifyCustomers();
    public List<PotentialCustomerResponseDto> getPotentialCustomers();
}
