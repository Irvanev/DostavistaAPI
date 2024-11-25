package com.delivery.dostavista.services.impl;

import com.delivery.dostavista.dtos.orderCourierDtos.CreateOrderCourierDto;
import com.delivery.dostavista.dtos.orderCourierDtos.OrderCourierDto;
import com.delivery.dostavista.models.OrderStatusMessage;
import com.delivery.dostavista.models.entities.Couriers;
import com.delivery.dostavista.models.entities.Orders;
import com.delivery.dostavista.models.entities.OrdersCourier;
import com.delivery.dostavista.models.enums.OrderStatusEnum;
import com.delivery.dostavista.repositories.CourierRepository;
import com.delivery.dostavista.repositories.OrderCourierRepository;
import com.delivery.dostavista.repositories.OrderRepository;
import com.delivery.dostavista.services.OrderCourierService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderCourierServiceImpl implements OrderCourierService {

    private final OrderCourierRepository orderCourierRepository;
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final ModelMapper modelMapper;
    private final RabbitTemplate rabbitTemplate;

    public OrderCourierServiceImpl(
            OrderCourierRepository orderCourierRepository,
            OrderRepository orderRepository,
            CourierRepository courierRepository,
            ModelMapper modelMapper,
            RabbitTemplate rabbitTemplate
    ) {
        this.orderCourierRepository = orderCourierRepository;
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.modelMapper = modelMapper;
        this.rabbitTemplate = rabbitTemplate;
    }


    @Transactional
    public OrderCourierDto createOrderCourier(CreateOrderCourierDto createOrderCourierDto) {
        OrdersCourier ordersCourier = new OrdersCourier();

        Couriers courier = courierRepository.findById(createOrderCourierDto.getCourierId())
                .orElseThrow(() -> new RuntimeException("Courier not found"));
        ordersCourier.setCourier(courier);

        Orders order = orderRepository.findById(createOrderCourierDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        ordersCourier.setOrder(order);

        ordersCourier.setStatus(OrderStatusEnum.TAKEN);
        ordersCourier.setAssignedAt(new Date());

        order.setStatus(OrderStatusEnum.TAKEN);
        orderRepository.save(order);

        return modelMapper.map(orderCourierRepository.saveAndFlush(ordersCourier), OrderCourierDto.class);
    }


    @Override
    public Page<OrderCourierDto> getAllOrdersCourier(Pageable pageable) {
        Page<OrdersCourier> ordersCourierPage = orderCourierRepository.findAll(pageable);
        return ordersCourierPage.map(ordersCourier -> modelMapper.map(ordersCourier, OrderCourierDto.class));
    }

    @Override
    public Optional<OrderCourierDto> getOrderCourier(Long id) {
        return orderCourierRepository.findById(id).map(ordersCourier -> modelMapper.map(ordersCourier, OrderCourierDto.class));
    }

    @Override
    public void deleteOrderCourier(Long id) {
        orderCourierRepository.deleteById(id);
    }

    @Override
    public OrderCourierDto editOrderCourier(Long id, CreateOrderCourierDto createOrderCourierDto) {
        OrdersCourier ordersCourier = orderCourierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        modelMapper.map(createOrderCourierDto, ordersCourier);

        return modelMapper.map(orderCourierRepository.saveAndFlush(ordersCourier), OrderCourierDto.class);
    }

    @Transactional
    @Override
    public OrderCourierDto updateOrderStatusByCourier(Long courierId, Long orderId, OrderStatusEnum newStatus) {
        OrdersCourier ordersCourier = orderCourierRepository.findByCourierIdAndOrderId(courierId, orderId)
                .orElseThrow(() -> new RuntimeException("Order not assigned to this courier"));

        Orders order = orderRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException("Order not found"));

        Couriers courier = courierRepository.findById(courierId).orElseThrow(
                () -> new RuntimeException("Courier not found"));

        order.setStatus(newStatus);

        ordersCourier.setStatus(newStatus);
        orderCourierRepository.save(ordersCourier);

        if (newStatus == OrderStatusEnum.COMPLETED) {
            OrderStatusMessage message = new OrderStatusMessage(courierId, order.getPrice(), courier.getRequisite());
            rabbitTemplate.convertAndSend("testExchange", "my.key", message);
            System.out.println(message);
        }

        return modelMapper.map(ordersCourier, OrderCourierDto.class);
    }
}
