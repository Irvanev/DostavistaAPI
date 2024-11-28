package com.delivery.dostavista.graphql;

import com.delivery.dostavista.dtos.orderCourierDtos.CreateOrderCourierDto;
import com.delivery.dostavista.dtos.orderCourierDtos.OrderCourierDto;
import com.delivery.dostavista.models.entities.OrdersCourier;
import com.delivery.dostavista.repositories.OrderCourierRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DgsComponent
public class OrderCourierDataFetcher {

    private final OrderCourierRepository orderCourierRepository;
    private final ModelMapper modelMapper;

    public OrderCourierDataFetcher(OrderCourierRepository orderCourierRepository, ModelMapper modelMapper) {
        this.orderCourierRepository = orderCourierRepository;
        this.modelMapper = modelMapper;
    }

    @DgsQuery
    public Optional<OrderCourierDto> findOrderCourierById(@InputArgument Long id) {
        return orderCourierRepository.findById(id).map(ordersCourier -> modelMapper.map(ordersCourier, OrderCourierDto.class));
    }

    @DgsQuery
    public List<OrderCourierDto> findAllOrdersCourier() {
        return orderCourierRepository.findAll().stream().map(ordersCourier -> modelMapper.map(ordersCourier, OrderCourierDto.class))
                .collect(Collectors.toList());
    }

    @DgsMutation
    public OrderCourierDto createOrderCourier(CreateOrderCourierDto createFeedbackDto) {
        OrdersCourier ordersCourier = modelMapper.map(createFeedbackDto, OrdersCourier.class);

        ordersCourier.setAssignedAt(new Date());

        return modelMapper.map(orderCourierRepository.saveAndFlush(ordersCourier), OrderCourierDto.class);
    }

    @DgsMutation
    public boolean deleteOrderCourier(Long id) {
        orderCourierRepository.deleteById(id);
        return true;
    }
}
