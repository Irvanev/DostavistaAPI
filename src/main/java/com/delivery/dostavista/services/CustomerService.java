package com.delivery.dostavista.services;

import com.delivery.dostavista.dtos.customerDtos.CustomerDto;
import com.delivery.dostavista.dtos.customerDtos.CreateCustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerService {
    CustomerDto createCustomers(CreateCustomerDto createCustomerDto);
    void deleteCustomer(Long id);
    CustomerDto editCustomer(Long id, CreateCustomerDto createCustomerDto);
    Page<CustomerDto> getAllCustomers(Pageable pageable);
    Optional<CustomerDto> getCustomer(Long id);
}
