package com.example.customersupport.service;

import com.example.customersupport.entity.Customer;
import com.example.customersupport.entity.User;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepo customerRepo;

    public Customer findCustomerByUser(User user) {
        return customerRepo.findCustomerByUser(user).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"customer is not there with the given id"));
    }
}
