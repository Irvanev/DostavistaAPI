package com.delivery.dostavista.graphql;

import com.delivery.dostavista.dtos.courierDtos.CourierDto;
import com.delivery.dostavista.dtos.courierDtos.CreateCourierDto;
import com.delivery.dostavista.models.entities.Couriers;
import com.delivery.dostavista.repositories.CourierRepository;
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
public class CourierDataFetcher {

    private final CourierRepository courierRepository;
    private final ModelMapper modelMapper;

    public CourierDataFetcher(CourierRepository courierRepository, ModelMapper modelMapper) {
        this.courierRepository = courierRepository;
        this.modelMapper = modelMapper;
    }

    @DgsQuery
    public Optional<CourierDto> findCourierById(@InputArgument Long id) {
        return courierRepository.findById(id).map(courier -> modelMapper.map(courier, CourierDto.class));
    }

    @DgsQuery
    public List<CourierDto> findAllCouriers() {
        return courierRepository.findAll().stream().map(courier -> modelMapper.map(courier, CourierDto.class))
                .collect(Collectors.toList());
    }

    @DgsMutation
    public CourierDto createCourier(CreateCourierDto createCourierDto) {
        Couriers couriers = modelMapper.map(createCourierDto, Couriers.class);

        couriers.setCreatedAt(new Date());

        return modelMapper.map(courierRepository.saveAndFlush(couriers), CourierDto.class);
    }

    @DgsMutation
    public boolean deleteCourier(Long id) {
        courierRepository.deleteById(id);
        return true;
    }
}
