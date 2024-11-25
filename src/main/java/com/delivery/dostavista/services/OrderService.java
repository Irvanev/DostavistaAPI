package com.delivery.dostavista.services;

import com.delivery.dostavista.dtos.orderDtos.CreateOrderDto;
import com.delivery.dostavista.dtos.orderDtos.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {
    OrderDto createOrder(CreateOrderDto createOrderDto);
    Optional<OrderDto> getOrderById(Long id);
    Page<OrderDto> getAllOrders(Pageable pageable);
    void deleteOrder(Long id);
    OrderDto editOrder(Long id, CreateOrderDto createOrderDto);
}
