package com.delivery.dostavista.services.impl;

import com.delivery.dostavista.dtos.orderDtos.CreateOrderDto;
import com.delivery.dostavista.dtos.orderDtos.OrderDto;
import com.delivery.dostavista.models.entities.Orders;
import com.delivery.dostavista.models.enums.OrderStatusEnum;
import com.delivery.dostavista.repositories.OrderRepository;
import com.delivery.dostavista.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public OrderDto createOrder(CreateOrderDto createOrderDto) {
        Orders order = modelMapper.map(createOrderDto, Orders.class);
        order.setCreatedAt(new Date());
        order.setStatus(OrderStatusEnum.CREATED);

        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }

    @Override
    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id).map(orderDto -> modelMapper.map(orderDto, OrderDto.class));
    }

    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        Page<Orders> ordersPage = orderRepository.findAll(pageable);
        return ordersPage.map(order -> modelMapper.map(order, OrderDto.class));
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public OrderDto editOrder(Long id, CreateOrderDto createOrderDto) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));

        modelMapper.map(createOrderDto, order);

        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }
}
