package com.delivery.dostavista.services;

import com.delivery.dostavista.dtos.orderCourierDtos.CreateOrderCourierDto;
import com.delivery.dostavista.dtos.orderCourierDtos.OrderCourierDto;
import com.delivery.dostavista.models.enums.OrderStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderCourierService {
    OrderCourierDto createOrderCourier(CreateOrderCourierDto createOrderCourierDto);
    Page<OrderCourierDto> getAllOrdersCourier(Pageable pageable);
    Optional<OrderCourierDto> getOrderCourier(Long id);
    void deleteOrderCourier(Long id);
    OrderCourierDto editOrderCourier(Long id, CreateOrderCourierDto createOrderCourierDto);
    OrderCourierDto updateOrderStatusByCourier(Long courierId, Long orderId, OrderStatusEnum newStatus);
}
